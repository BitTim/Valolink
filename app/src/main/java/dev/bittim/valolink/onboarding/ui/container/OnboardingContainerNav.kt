/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:26
 */

package dev.bittim.valolink.onboarding.ui.container

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object OnboardingContainerNav

fun NavGraphBuilder.onboarding(
    navContent: () -> Unit
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
            navContent = navContent,
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
