package dev.bittim.valolink.feature.content.ui.matches

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.ui.theme.Transition

const val MatchesNavRoute = "matches"

fun NavGraphBuilder.matchesScreen() {
    composable(
        route = MatchesNavRoute,
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        val viewModel: MatchesViewModel = hiltViewModel()
        val matchesState by viewModel.matchesState.collectAsStateWithLifecycle()

        MatchesScreen(
            state = matchesState,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToMatches() {
    navigate(MatchesNavRoute) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}