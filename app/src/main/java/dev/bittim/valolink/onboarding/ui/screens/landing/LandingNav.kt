/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LandingNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.onboarding.ui.screens.landing

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object LandingNav

fun NavGraphBuilder.landingScreen(
    navSignin: () -> Unit,
    navLocalMode: () -> Unit
) {
    composable<LandingNav> {
        val viewModel: LandingViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        LandingScreen(
            state = state.value,
            onLocalMode = navLocalMode,
            onGoogleClicked = { },
            onRiotClicked = { },
            onEmailClicked = navSignin
        )
    }
}

fun NavController.navToOnboardingLanding() {
    navigate(LandingNav) {
        popUpTo(LandingNav) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
