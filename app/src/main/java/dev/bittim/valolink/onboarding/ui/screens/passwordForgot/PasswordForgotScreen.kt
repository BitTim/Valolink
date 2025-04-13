/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PasswordForgotScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:57
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordForgot

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.SimpleLoadingContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.core.ui.util.autofill.autofillRequestHandler
import dev.bittim.valolink.core.ui.util.extensions.connectNode
import dev.bittim.valolink.core.ui.util.extensions.defaultFocusChangeAutoFill
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout

data object PasswordForgotScreen {
    const val SPRAY_UUID: String = "51dc5786-4f73-8a5b-fd04-8eb6e78b9d74"
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PasswordForgotScreen(
    state: PasswordForgotState,
    validateEmail: (email: String) -> Unit,
    onCancel: () -> Unit,
    onContinue: (email: String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    val onEmailChanged = { value: String ->
        email = value
        validateEmail(value)
    }

    val emailAutoFillHandler =
        autofillRequestHandler(
            autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = onEmailChanged
        )

    OnboardingLayout(
        modifier = Modifier.fillMaxSize(),
        content = {
            SimpleLoadingContainer(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.loading,
                label = "Spray image loading crossfade"
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.l),
                    model = state.spray?.fullTransparentIcon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopCenter,
                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon),
                )
            }
        },
        form = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .connectNode(emailAutoFillHandler)
                        .defaultFocusChangeAutoFill(emailAutoFillHandler),
                    label = UiText.StringResource(R.string.onboarding_textField_label_email)
                        .asString(),
                    value = email,
                    error = state.emailError,
                    onValueChange = {
                        if (it.isEmpty()) emailAutoFillHandler.requestVerifyManual()
                        onEmailChanged(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = onCancel,
                    onContinue = { onContinue(email) },
                    disableContinueButton = state.emailError != null,
                    dismissText = UiText.StringResource(R.string.button_cancel),
                    continueText = UiText.StringResource(R.string.onboarding_passwordForgot_button_send)
                )
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@ScreenPreviewAnnotations
@Composable
fun PasswordForgotScreenPreview() {
    ValolinkTheme {
        Surface {
            PasswordForgotScreen(
                state = PasswordForgotState(),
                validateEmail = {},
                onCancel = {},
                onContinue = {},
            )
        }
    }
}
