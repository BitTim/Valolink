/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CreateAccountNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
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
                viewModel.createUser(email, password)
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
