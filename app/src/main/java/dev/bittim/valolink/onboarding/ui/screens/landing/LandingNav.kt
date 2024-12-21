/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LandingNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 01:24
 */

package dev.bittim.valolink.onboarding.ui.screens.landing

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object LandingNav

fun NavGraphBuilder.landingScreen(
    navSignin: () -> Unit
) {
    composable<LandingNav> {
        val viewModel: LandingViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        LandingScreen(
            state = state.value,
            onLocalClicked = { },
            onGoogleClicked = { },
            onRiotClicked = { },
            onEmailClicked = navSignin
        )
    }
}

fun NavController.navToOnboardingLanding() {
    navigate(LandingNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}