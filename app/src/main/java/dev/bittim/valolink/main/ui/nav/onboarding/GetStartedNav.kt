package dev.bittim.valolink.main.ui.nav.onboarding

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.main.ui.screens.onboarding.getstarted.GetStartedScreen
import dev.bittim.valolink.main.ui.screens.onboarding.getstarted.GetStartedViewModel
import kotlinx.serialization.Serializable

@Serializable
object GetStartedNav

fun NavGraphBuilder.getStartedScreen(
    onNavToContent: () -> Unit,
) {
    composable<GetStartedNav> {
        val viewModel: GetStartedViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        GetStartedScreen(
            state = state.value,
            onGetStartedClicked = { viewModel.onGetStartedClicked(onNavToContent) },
        )
    }
}