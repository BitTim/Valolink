package dev.bittim.valolink.onboarding.ui.screens.landing

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.onboarding.ui.components.OnboardingScreen

data object LandingScreen {
    const val SPRAY_UUID: String = "40cc1645-43f4-4db3-ebb2-fdb46f8e9bf3"
}

@Composable
fun LandingScreen(
    state: LandingState,
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
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = state.spray?.fullTransparentIcon,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon),
            )
        },
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onGoogleClicked
                ) {
                    Text("Continue with Google")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRiotClicked
                ) {
                    Text("Continue with Riot")
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
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

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LandingScreenPreview() {
    ValolinkTheme {
        Surface {
            LandingScreen(
                state = LandingState(),
                onLocalClicked = {},
                onGoogleClicked = {},
                onRiotClicked = {},
                onEmailClicked = {}
            )
        }
    }
}