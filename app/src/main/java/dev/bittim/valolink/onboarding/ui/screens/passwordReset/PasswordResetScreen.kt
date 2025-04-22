/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PasswordResetScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordReset

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

data object PasswordResetScreen {
    const val SPRAY_UUID: String = "51dc5786-4f73-8a5b-fd04-8eb6e78b9d74"
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PasswordResetScreen(
    state: PasswordResetState,
    validatePassword: (password: String) -> Unit,
    onCancel: () -> Unit,
    onContinue: (password: String) -> Unit,
) {
    var password by remember { mutableStateOf("") }
    val onPasswordChanged = { value: String ->
        password = value
        validatePassword(value)
    }

    val passwordAutofillHandler =
        autofillRequestHandler(
            autofillTypes = listOf(AutofillType.NewPassword),
            onFill = onPasswordChanged
        )

    OnboardingLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.l),
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
                        .connectNode(passwordAutofillHandler)
                        .defaultFocusChangeAutoFill(passwordAutofillHandler),
                    label = UiText.StringResource(R.string.onboarding_textField_label_password)
                        .asString(),
                    value = password,
                    visibility = false,
                    error = state.passwordError,
                    onValueChange = {
                        if (it.isEmpty()) passwordAutofillHandler.requestVerifyManual()
                        onPasswordChanged(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = onCancel,
                    onContinue = { onContinue(password) },
                    disableContinueButton = state.passwordError != null,
                    dismissText = UiText.StringResource(R.string.button_cancel),
                    continueText = UiText.StringResource(R.string.onboarding_passwordReset_button_send)
                )
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@ScreenPreviewAnnotations
@Composable
fun PasswordResetScreenPreview() {
    ValolinkTheme {
        Surface {
            PasswordResetScreen(
                state = PasswordResetState(),
                validatePassword = {},
                onCancel = {},
                onContinue = {},
            )
        }
    }
}
