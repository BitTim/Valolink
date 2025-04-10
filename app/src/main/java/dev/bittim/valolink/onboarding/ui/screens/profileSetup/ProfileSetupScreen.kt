/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   11.04.25, 00:25
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.LabeledSwitch
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.SimpleLoadingContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import dev.bittim.valolink.onboarding.ui.dialogs.profileSetup.PrivateAccountInfoDialog

@Composable
fun ProfileSetupScreen(
    state: ProfileSetupState,
    localMode: Boolean = false,
    validateUsername: (username: String) -> Unit,
    onBack: () -> Unit,
    setProfile: (username: String, private: Boolean) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var private by remember { mutableStateOf(false) }

    val onUsernameChanged = { value: String ->
        username = value
        validateUsername(value)
    }

    var showPrivateAccountInfoDialog by remember { mutableStateOf(false) }

    OnboardingLayout(
        modifier = Modifier.fillMaxSize(),
        content = {
            SimpleLoadingContainer(
                modifier = Modifier
                    .fillMaxSize(),
                isLoading = state.loading,
                label = "Spray image loading crossfade"
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.l)
                        .clip(CircleShape),
                    model = state.avatar,
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
                        .fillMaxWidth(),
                    label = UiText.StringResource(R.string.onboarding_textField_label_username)
                        .asString(),
                    value = username,
                    error = state.usernameError,
                    onValueChange = onUsernameChanged,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                if (!localMode) {
                    LabeledSwitch(
                        modifier = Modifier.fillMaxWidth(),
                        label = UiText.StringResource(R.string.onboarding_switch_label_private)
                            .asString(),
                        value = private,
                        onValueChange = { private = it },
                        showTooltip = true,
                        onTooltip = {
                            showPrivateAccountInfoDialog = true
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = onBack,
                    onContinue = { setProfile(username, private) }, // TODO: Include profile pic
                    dismissText = UiText.StringResource(R.string.button_cancel),
                    continueText = UiText.StringResource(R.string.button_continue)
                )
            }
        }
    )

    // ================================
    //  Alerts
    // ================================

    if (showPrivateAccountInfoDialog) {
        PrivateAccountInfoDialog(
            onDismiss = { showPrivateAccountInfoDialog = false }
        )
    }
}

@ScreenPreviewAnnotations
@Composable
fun ProfileSetupScreenPreview() {
    ValolinkTheme {
        Surface {
            ProfileSetupScreen(
                state = ProfileSetupState(),
                validateUsername = { },
                onBack = { },
                setProfile = { _, _ -> }
            )
        }
    }
}
