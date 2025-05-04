/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 09:34
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ProfileSetupNav

fun NavGraphBuilder.profileSetupScreen() {
    composable<ProfileSetupNav> { backStackEntry ->
        val viewModel: ProfileSetupViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        ProfileSetupScreen(
            state = state.value,
            onUsernameChanged = viewModel::onUsernameChanged,
            onPrivateChanged = viewModel::onPrivateChanged,
            navLanding = {
                viewModel.signOut()
            },
            setProfile = {
                viewModel.setProfile(state.value.username, state.value.private, state.value.avatar)
            },
            selectAvatar = viewModel::selectAvatar,
            resetAvatar = viewModel::resetAvatar
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
