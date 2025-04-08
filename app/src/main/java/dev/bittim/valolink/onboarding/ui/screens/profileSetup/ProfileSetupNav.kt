/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 01:07
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ProfileSetupNav

fun NavGraphBuilder.profileSetupScreen(
    navBack: () -> Unit,
    navRankSetup: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<ProfileSetupNav> {
        val viewModel: ProfileSetupViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        ProfileSetupScreen(
            state = state.value,
            viewModel::validateUsername,
            onBack = navBack,
            setProfile = { username, private ->
                viewModel.setProfile(username, private, navRankSetup)
            }
        )
    }
}

fun NavController.navOnboardingProfileSetup() {
    navigate(ProfileSetupNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
