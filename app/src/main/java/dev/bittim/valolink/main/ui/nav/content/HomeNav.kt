package dev.bittim.valolink.main.ui.nav.content

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.home.HomeScreen
import dev.bittim.valolink.main.ui.screens.content.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
object HomeNav

fun NavGraphBuilder.homeScreen(
    onSignOutClicked: () -> Unit,
) {
    composable<HomeNav>(enterTransition = { Transition.topLevelEnter },
                        exitTransition = { Transition.topLevelExit },
                        popEnterTransition = { Transition.topLevelEnter },
                        popExitTransition = { Transition.topLevelExit }) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = homeState,
            onSignOutClicked = onSignOutClicked
        )
    }
}

fun NavController.navToHome() {
    navigate(HomeNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}