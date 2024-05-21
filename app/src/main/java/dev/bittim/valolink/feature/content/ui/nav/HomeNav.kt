package dev.bittim.valolink.feature.content.ui.nav

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.feature.content.ui.screens.home.HomeScreen
import dev.bittim.valolink.feature.content.ui.screens.home.HomeViewModel
import dev.bittim.valolink.ui.theme.Transition
import kotlinx.serialization.Serializable

@Serializable
object HomeNav

fun NavGraphBuilder.homeScreen(
    onSignOutClicked: () -> Unit
) {
    composable<HomeNav>(
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()

        HomeScreen(
            state = homeState,
            onFetch = viewModel::onFetch,
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