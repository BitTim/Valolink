/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   06.04.25, 12:05
 */

package dev.bittim.valolink.onboarding.ui.container

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.bittim.valolink.main.ui.nav.navToMainGraph
import kotlinx.serialization.Serializable

@Serializable
object OnboardingContainerNav

fun NavGraphBuilder.onboarding(
    navController: NavController
) {
    composable<OnboardingContainerNav> {
        val viewModel = hiltViewModel<OnboardingContainerViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()

        val subNavController = rememberNavController()
        subNavController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.onDestinationChanged(destination.route ?: "")
        }

        val snackbarHostState = viewModel.snackbarHostState.collectAsStateWithLifecycle()

        OnboardingContainerScreen(
            state = state.value,
            navController = subNavController,
            snackbarHostState = snackbarHostState.value,
            navMain = { navController.navToMainGraph() },
        )
    }
}

fun NavController.navToOnboarding() {
    navigate(OnboardingContainerNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
