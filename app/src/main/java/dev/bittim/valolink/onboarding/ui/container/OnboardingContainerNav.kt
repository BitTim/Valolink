/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.12.24, 21:00
 */

package dev.bittim.valolink.onboarding.ui.container

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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

        OnboardingContainer(
            state = state.value,
            navController = subNavController,
            onNavToMain = { navController.navToMainGraph() }
        )
    }
}