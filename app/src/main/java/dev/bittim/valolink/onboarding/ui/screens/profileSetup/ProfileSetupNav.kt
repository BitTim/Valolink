/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ProfileSetupNav

fun NavGraphBuilder.profileSetupScreen(
    navBack: () -> Unit,
    navRankSetup: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    composable<ProfileSetupNav> { backStackEntry ->
        val viewModel: ProfileSetupViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        ProfileSetupScreen(
            state = state.value,
            validateUsername = viewModel::validateUsername,
            onBack = {
                viewModel.signOut()
                navBack()
            },
            setProfile = { username, private ->
                viewModel.setProfile(username, private, state.value.avatar, navRankSetup)
            },
            selectAvatar = viewModel::selectAvatar,
        )
    }
}

fun NavController.navOnboardingProfileSetup() {
    navigate(ProfileSetupNav) {
        launchSingleTop = true
        restoreState = true
    }
}
