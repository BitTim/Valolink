/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LandingScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.onboarding.ui.screens.landing

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.SimpleLoadingContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import dev.bittim.valolink.onboarding.ui.components.landing.OutlinedSocialButton
import dev.bittim.valolink.onboarding.ui.dialogs.landing.LocalAccountDialog

data object LandingScreen {
    const val SPRAY_UUID: String = "40cc1645-43f4-4db3-ebb2-fdb46f8e9bf3"
}

@Composable
fun LandingScreen(
    state: LandingState,
    onLocalMode: () -> Unit,
    onGoogleClicked: () -> Unit,
    onRiotClicked: () -> Unit,
    onEmailClicked: () -> Unit
) {
    var showLocalAccountDialog by remember { mutableStateOf(false) }

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
                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon),
                )
            }
        },
        form = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedSocialButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onGoogleClicked,
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google",
                    text = UiText.StringResource(R.string.onboarding_landing_button_google)
                        .asString(),
                )

                //TODO: This button will be enabled when Riot Games integration will be implemented
                OutlinedSocialButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRiotClicked,
                    painter = painterResource(id = R.drawable.ic_riot_logo),
                    contentDescription = "Riot",
                    text = UiText.StringResource(R.string.onboarding_landing_button_riot)
                        .asString(),
                    enabled = false
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = Spacing.s)
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showLocalAccountDialog = true }
                ) {
                    Text(UiText.StringResource(R.string.onboarding_landing_button_local).asString())
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onEmailClicked
                ) {
                    Text(
                        UiText.StringResource(R.string.onboarding_landing_button_email).asString(),
                    )
                }
            }
        }
    )

    // ================================
    //  Alerts
    // ================================

    if (showLocalAccountDialog) {
        LocalAccountDialog(
            onDismiss = { showLocalAccountDialog = false },
            onConfirm = onLocalMode
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@ScreenPreviewAnnotations
@Composable
fun LandingScreenPreview() {
    ValolinkTheme {
        Surface {
            LandingScreen(
                state = LandingState(),
                onLocalMode = {},
                onGoogleClicked = {},
                onRiotClicked = {},
                onEmailClicked = {}
            )
        }
    }
}
