package dev.bittim.valolink.feature.content.ui.matches

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val MatchesNavRoute = "matches"

fun NavGraphBuilder.matchesScreen() {
    composable(MatchesNavRoute) {
        val viewModel: MatchesViewModel = hiltViewModel()
        val matchesState by viewModel.matchesState.collectAsStateWithLifecycle()

        MatchesScreen(
            state = matchesState,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToMatches(origin: String) {
    navigate(MatchesNavRoute) {
        popUpTo(origin) {
            inclusive = true
        }
        launchSingleTop = true
    }
}