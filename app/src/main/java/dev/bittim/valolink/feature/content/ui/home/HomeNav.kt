package dev.bittim.valolink.feature.content.ui.home

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HomeNavRoute = "home"

fun NavGraphBuilder.homeScreen(
    onSignOutClicked: () -> Unit
) {
    composable(HomeNavRoute) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()

        HomeScreen(
            state = homeState,
            onFetch = viewModel::onFetch,
            onSignOutClicked = onSignOutClicked
        )
    }
}

fun NavController.navToHome(origin: String) {
    navigate(HomeNavRoute) {
        popUpTo(origin) {
            inclusive = true
        }
        launchSingleTop = true
    }
}