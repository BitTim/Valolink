/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LandingScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.onboarding.ui.screens.landing

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
import dev.bittim.valolink.onboarding.ui.components.OnboardingScreen
import dev.bittim.valolink.onboarding.ui.components.landing.OutlinedSocialButton

data object LandingScreen {
    const val SPRAY_UUID: String = "40cc1645-43f4-4db3-ebb2-fdb46f8e9bf3"
    const val PROGRESS: Float = 0f
}

@Composable
fun LandingScreen(
    state: LandingState,
    onLegacyClicked: () -> Unit, // TEMP
    onLocalClicked: () -> Unit,
    onGoogleClicked: () -> Unit,
    onRiotClicked: () -> Unit,
    onEmailClicked: () -> Unit
) {
    OnboardingScreen(
        modifier = Modifier.fillMaxSize(),
        title = UiText.StringResource(R.string.onboarding_landing_title).asString(),
        progress = LandingScreen.PROGRESS,
        description = UiText.StringResource(R.string.onboarding_landing_description).asString(),
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
                // TEMP
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLegacyClicked
                ) {
                    Text("Use legacy login")
                }

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
                    onClick = onLocalClicked
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
}

@ScreenPreviewAnnotations
@Composable
fun LandingScreenPreview() {
    ValolinkTheme {
        Surface {
            LandingScreen(
                state = LandingState(),
                onLegacyClicked = {},
                onLocalClicked = {},
                onGoogleClicked = {},
                onRiotClicked = {},
                onEmailClicked = {}
            )
        }
    }
}