/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
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
    snackbarHostState: SnackbarHostState,
) {
    composable<ProfileSetupNav> { backStackEntry ->
        val viewModel: ProfileSetupViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        ProfileSetupScreen(
            state = state.value,
            validateUsername = viewModel::validateUsername,
            navLanding = {
                viewModel.signOut()
            },
            setProfile = { username, private ->
                viewModel.setProfile(username, private, state.value.avatar)
            },
            selectAvatar = viewModel::selectAvatar,
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
