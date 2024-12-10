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
import dev.bittim.valolink.core.ui.util.ScreenPreviewAnnotations
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.onboarding.ui.components.OnboardingScreen
import dev.bittim.valolink.onboarding.ui.components.landing.OutlinedSocialButton

data object LandingScreen {
    const val SPRAY_UUID: String = "40cc1645-43f4-4db3-ebb2-fdb46f8e9bf3"
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
        title = "Welcome",
        progress = 0f,
        description = "Welcome to Valolink! Start to get insights in your matches, battlepass progress and rank by signing in",
        content = {
            SimpleLoadingContainer(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.loading,
                label = "Spray image loading crossfade"
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
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
                    text = "Continue with Google"
                )

                //TODO: This button will be enabled when Riot Games integration will be implemented
                OutlinedSocialButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRiotClicked,
                    painter = painterResource(id = R.drawable.ic_riot_logo),
                    contentDescription = "Riot",
                    text = "Continue with Riot",
                    enabled = false
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = Spacing.s)
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLocalClicked
                ) {
                    Text("Use local account")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onEmailClicked
                ) {
                    Text("Continue with E-Mail")
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