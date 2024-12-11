package dev.bittim.valolink.onboarding.ui.screens.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.SimpleLoadingContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.onboarding.ui.components.OnboardingScreen
import dev.bittim.valolink.user.domain.usecase.validator.EmailError

data object SigninScreen {
    const val SPRAY_UUID: String = "dc5edd15-455d-1782-7ee3-a294a6a3d293"
    const val PROGRESS: Float = 1f / 6f
}

@Composable
fun SigninScreen(
    state: SigninState,
    validateEmail: (email: String) -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onContinueClicked: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    OnboardingScreen(
        modifier = Modifier.fillMaxSize(),
        title = UiText.StringResource(R.string.onboarding_signin_title).asString(),
        progress = SigninScreen.PROGRESS,
        description = UiText.StringResource(R.string.onboarding_signin_description).asString(),
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
                        .imePadding(),
                    label = UiText.StringResource(R.string.onboarding_signin_label_email)
                        .asString(),
                    value = email,
                    error = when (state.emailResult) {
                        is Result.Failure -> {
                            when (state.emailResult.error) {
                                EmailError.EMPTY -> UiText.StringResource(R.string.error_empty_email)
                                    .asString()

                                EmailError.INVALID -> UiText.StringResource(R.string.error_invalid_email)
                                    .asString()
                            }
                        }

                        else -> null
                    },
                    onValueChange = {
                        email = it
                        validateEmail(it)
                    }
                )

                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding(),
                    label = UiText.StringResource(R.string.onboarding_signin_label_password)
                        .asString(),
                    value = password,
                    error = null, // TODO: Add password error
                    onValueChange = { password = it },
                    enableVisibilityToggle = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onForgotPasswordClicked) {
                        Text(
                            text = UiText.StringResource(R.string.onboarding_signin_button_forgot_password)
                                .asString()
                        )
                    }

                    TextButton(onClick = onCreateAccountClicked) {
                        Text(
                            text = UiText.StringResource(R.string.onboarding_signin_button_create_account)
                                .asString()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = onCancelClicked) {
                        Text(text = UiText.StringResource(R.string.button_cancel).asString())
                    }

                    Button(onClick = onContinueClicked) {
                        Text(text = UiText.StringResource(R.string.button_continue).asString())
                    }
                }
            }
        }
    )
}

@ScreenPreviewAnnotations
@Composable
fun SigninScreenPreview() {
    ValolinkTheme {
        Surface {
            SigninScreen(
                state = SigninState(),
                validateEmail = {},
                onForgotPasswordClicked = {},
                onCreateAccountClicked = {},
                onCancelClicked = {},
                onContinueClicked = {},
            )
        }
    }
}