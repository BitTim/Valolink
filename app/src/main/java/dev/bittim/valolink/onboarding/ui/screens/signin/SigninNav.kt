/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SigninNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.onboarding.ui.screens.signin

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SigninNav

fun NavGraphBuilder.signinScreen(
    navBack: () -> Unit,
    navMain: () -> Unit,
    navPasswordForgot: () -> Unit,
    navCreateAccount: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    composable<SigninNav> {
        val viewModel: SigninViewModel = hiltViewModel()
        viewModel.setSnackbarHostState(snackbarHostState)

        val state = viewModel.state.collectAsStateWithLifecycle()

        SigninScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            onForgotPassword = navPasswordForgot,
            onCreateAccount = navCreateAccount,
            onCancel = navBack,
            signin = { email, password ->
                viewModel.signIn(email, password, navMain)
            }
        )
    }
}

fun NavController.navOnboardingSignin() {
    navigate(SigninNav) {
        launchSingleTop = true
        restoreState = true
    }
}
