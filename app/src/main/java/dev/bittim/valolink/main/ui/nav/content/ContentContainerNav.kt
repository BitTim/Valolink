package dev.bittim.valolink.main.ui.nav.content

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.bittim.valolink.main.ui.screens.content.container.ContentContainerScreen
import dev.bittim.valolink.main.ui.screens.content.container.ContentContainerViewModel
import kotlinx.serialization.Serializable

@Serializable
object ContentContainerNav

fun NavGraphBuilder.contentContainerScreen(
    navToAuthGraph: () -> Unit,
    navToOnboardingGraph: () -> Unit,
) {
    composable<ContentContainerNav> {
        val viewModel: ContentContainerViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ContentContainerScreen(
            state = state,
            navController = rememberNavController(),
            navToAuthGraph = navToAuthGraph,
            navToOnboardingGraph = navToOnboardingGraph,
            onSignOutClicked = viewModel::onSignOutClicked
        )
    }
}

fun NavController.navToContentContainer() {
    navigate(ContentContainerNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}