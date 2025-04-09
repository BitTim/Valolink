/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CreateAccountNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.onboarding.ui.screens.createAccount

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CreateAccountNav

fun NavGraphBuilder.createAccountScreen(
    navBack: () -> Unit,
    navProfileSetup: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<CreateAccountNav> {
        val viewModel: CreateAccountViewModel = hiltViewModel()
        viewModel.setSnackbarHostState(snackbarHostState)

        val state = viewModel.state.collectAsStateWithLifecycle()

        CreateAccount(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            validatePassword = viewModel::validatePassword,
            onCancel = navBack,
            createAccount = { email, password ->
                viewModel.createUser(email, password, navProfileSetup)
            }
        )
    }
}

fun NavController.navOnboardingCreateAccount() {
    navigate(CreateAccountNav) {
        launchSingleTop = true
        restoreState = true
    }
}
