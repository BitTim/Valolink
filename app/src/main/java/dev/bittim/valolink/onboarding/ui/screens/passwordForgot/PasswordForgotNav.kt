/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PasswordForgotNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordForgot

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PasswordForgotNav

fun NavGraphBuilder.passwordForgotScreen(
    navBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<PasswordForgotNav> {
        val viewModel: PasswordForgotViewModel = hiltViewModel()
        viewModel.setSnackbarHostState(snackbarHostState)

        val state = viewModel.state.collectAsStateWithLifecycle()

        PasswordForgotScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            onCancel = navBack,
            onContinue = { email ->
                viewModel.forgotPassword(email, navBack)
            }
        )
    }
}

fun NavController.navOnboardingPasswordForgot() {
    navigate(PasswordForgotNav) {
        launchSingleTop = true
        restoreState = true
    }
}
