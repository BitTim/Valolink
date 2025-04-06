/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PasswordResetNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   06.04.25, 12:16
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordReset

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
object PasswordResetNav


fun NavGraphBuilder.passwordResetScreen(
    navBack: () -> Unit,
    navMain: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<PasswordResetNav>(
        deepLinks = listOf(
            // TODO: Does not work in nested NavHost
            navDeepLink<PasswordResetNav>(
                basePath = "https://www.valolink.app"
            )
        )
    ) {
        val viewModel: PasswordResetViewModel = hiltViewModel()
        viewModel.setSnackbarHostState(snackbarHostState)

        val state = viewModel.state.collectAsStateWithLifecycle()

        PasswordResetScreen(
            state = state.value,
            validatePassword = viewModel::validatePassword,
            onCancel = navBack,
            onContinue = { email ->
                viewModel.resetPassword(email, navMain)
            }
        )
    }
}
