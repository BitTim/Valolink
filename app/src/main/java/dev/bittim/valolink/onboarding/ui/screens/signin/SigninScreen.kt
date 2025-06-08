/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SigninScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 16:03
 */

package dev.bittim.valolink.onboarding.ui.screens.signin

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.autofill.contentType
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
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import dev.bittim.valolink.onboarding.ui.screens.signin.components.SigninButtons

data object SigninScreen {
    const val SPRAY_UUID: String = "dc5edd15-455d-1782-7ee3-a294a6a3d293"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SigninScreen(
    state: SigninState,
    validateEmail: (email: String) -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onCancel: () -> Unit,
    signin: (email: String, password: String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val onEmailChanged = { value: String ->
        email = value
        validateEmail(value)
    }

    val onPasswordChanged = { value: String ->
        password = value
    }

    OnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            SimpleLoadingContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.l),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.l),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .contentType(ContentType.Username),
                    label = UiText.StringResource(R.string.onboarding_textField_label_email)
                        .asString(),
                    value = email,
                    error = state.emailError,
                    onValueChange = onEmailChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .contentType(ContentType.Password),
                    label = UiText.StringResource(R.string.onboarding_textField_label_password)
                        .asString(),
                    value = password,
                    error = null,
                    onValueChange = onPasswordChanged,
                    enableVisibilityToggle = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { signin(email, password) }
                    )
                )

                SigninButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onForgotPassword = onForgotPassword,
                    onCreateAccount = onCreateAccount
                )

                Spacer(modifier = Modifier.height(Spacing.xl))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = onCancel,
                    onContinue = { signin(email, password) },
                    disableContinueButton = state.emailError != null,
                    dismissText = UiText.StringResource(R.string.button_cancel)
                )
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@ScreenPreviewAnnotations
@Composable
fun SigninScreenPreview() {
    ValolinkTheme {
        Surface {
            SigninScreen(
                state = SigninState(),
                validateEmail = {},
                onForgotPassword = {},
                onCreateAccount = {},
                onCancel = {},
                signin = { _, _ -> },
            )
        }
    }
}
