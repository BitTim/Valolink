/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSetupNav(val localMode: Boolean = false)

fun NavGraphBuilder.profileSetupScreen(
    navBack: () -> Unit,
    navRankSetup: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    composable<ProfileSetupNav> { backStackEntry ->
        val args: ProfileSetupNav = backStackEntry.toRoute()

        val viewModel: ProfileSetupViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        ProfileSetupScreen(
            state = state.value,
            localMode = args.localMode,
            validateUsername = viewModel::validateUsername,
            onBack = navBack,
            setProfile = { username, private ->
                viewModel.setProfile(username, private, navRankSetup)
            }
        )
    }
}

fun NavController.navOnboardingProfileSetup(localMode: Boolean = false) {
    navigate(ProfileSetupNav(localMode = localMode)) {
        launchSingleTop = true
        restoreState = true
    }
}
