/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 20:29
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.components.LabeledSwitch
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.SimpleLoadingContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import dev.bittim.valolink.onboarding.ui.screens.profileSetup.dialogs.PrivateAccountInfoDialog

@Composable
fun ProfileSetupScreen(
    state: ProfileSetupState,
    onUsernameChanged: (value: String) -> Unit,
    onPrivateChanged: (value: Boolean) -> Unit,
    selectAvatar: (username: String, context: Context?, uri: Uri?) -> Unit,
    resetAvatar: (username: String, context: Context?) -> Unit,
    navLanding: () -> Unit,
    setProfile: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated == false) navLanding()
    }

    var showPrivateAccountInfoDialog by remember { mutableStateOf(false) }

    val mediaPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            selectAvatar(state.username, context, uri)
        }
    }

    OnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.l)
            ) {
                SimpleLoadingContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = Spacing.l),
                    isLoading = state.avatar == null || state.isAuthenticated == null || state.isLocal == null,
                    label = "Avatar loading crossfade"
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .padding(Spacing.xxl)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        model = state.avatar,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FilledTonalIconButton(
                            modifier = Modifier
                                .width(48.dp)
                                .aspectRatio(1f),
                            onClick = {
                                resetAvatar(state.username, context)
                            }
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null)
                        }

                        FilledTonalIconButton(
                            modifier = Modifier
                                .width(48.dp)
                                .aspectRatio(1f),
                            onClick = {
                                mediaPicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                            }
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null)
                        }
                    }
                }
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
                        .fillMaxWidth(),
                    label = UiText.StringResource(R.string.onboarding_textField_label_username)
                        .asString(),
                    value = state.username,
                    error = state.usernameError,
                    onValueChange = onUsernameChanged,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                if (state.isLocal != true) {
                    LabeledSwitch(
                        modifier = Modifier.fillMaxWidth(),
                        label = UiText.StringResource(R.string.onboarding_switch_label_private)
                            .asString(),
                        value = state.private,
                        onValueChange = onPrivateChanged,
                        showTooltip = true,
                        onTooltip = {
                            showPrivateAccountInfoDialog = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xl))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = navLanding,
                    onContinue = setProfile,
                    disableContinueButton = state.isAuthenticated != true || state.avatar == null || state.avatarError != null || state.usernameError != null || state.username.isEmpty(),
                    dismissText = UiText.StringResource(R.string.onboarding_profileSetup_button_signout),
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
                onUsernameChanged = { },
                onPrivateChanged = { },
                navLanding = { },
                setProfile = { },
                selectAvatar = { _, _, _ -> },
                resetAvatar = { _, _ -> }
            )
        }
    }
}
