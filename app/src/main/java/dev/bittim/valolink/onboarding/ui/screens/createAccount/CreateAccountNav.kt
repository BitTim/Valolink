/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CreateAccountNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.12.24, 01:32
 */

package dev.bittim.valolink.onboarding.ui.screens.createAccount

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CreateAccountNav

fun NavGraphBuilder.createAccountScreen(
    navBack: () -> Unit,
    navMain: () -> Unit,
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
                viewModel.createUser(email, password, navMain)
            }
        )
    }
}

fun NavController.navOnboardingCreateAccount() {
    navigate(CreateAccountNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}