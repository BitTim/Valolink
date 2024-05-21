package dev.bittim.valolink.feature.content.ui.nav

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.bittim.valolink.feature.content.ui.screens.main.MainScreen
import dev.bittim.valolink.feature.content.ui.screens.main.MainViewModel
import kotlinx.serialization.Serializable

@Serializable
object MainNav

fun NavGraphBuilder.mainScreen(
    onNavAuthGraph: () -> Unit
) {
    composable<MainNav> {
        val viewModel: MainViewModel = hiltViewModel()
        val mainState by viewModel.mainState.collectAsStateWithLifecycle()

        MainScreen(
            state = mainState,
            navController = rememberNavController(),
            onNavAuthGraph = onNavAuthGraph,
            onSignOutClicked = viewModel::onSignOutClicked
        )
    }
}

fun NavController.navToContentMain() {
    navigate(MainNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}