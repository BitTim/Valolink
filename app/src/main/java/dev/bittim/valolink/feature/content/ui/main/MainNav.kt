package dev.bittim.valolink.feature.content.ui.main

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val MainNavRoute = "main"
fun NavGraphBuilder.mainScreen(
    onNavAuthGraph: () -> Unit
) {
    composable(MainNavRoute) {
        val viewModel: MainViewModel = hiltViewModel()
        val mainState by viewModel.mainState.collectAsStateWithLifecycle()

        MainScreen(
            state = mainState,
            navController = rememberNavController(),
            onCheckAuth = viewModel::onCheckAuth,
            onNavAuthGraph = onNavAuthGraph,
            onSignOutClicked = viewModel::onSignOutClicked
        )
    }
}

fun NavController.navToContentMain(origin: String) {
    navigate(MainNavRoute) {
        popUpTo(origin) {
            inclusive = true
        }
        launchSingleTop = true
    }
}