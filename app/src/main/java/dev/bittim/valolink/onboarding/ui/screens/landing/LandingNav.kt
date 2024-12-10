package dev.bittim.valolink.onboarding.ui.screens.landing

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object LandingNav

fun NavGraphBuilder.landingScreen(
    onNavToAuth: () -> Unit
) {
    composable<LandingNav> {
        val viewModel: LandingViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        LandingScreen(
            state = state.value,
            onLegacyClicked = onNavToAuth,
            onLocalClicked = { },
            onGoogleClicked = { },
            onRiotClicked = { },
            onEmailClicked = { }
        )
    }
}