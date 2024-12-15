/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SignInNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.auth.ui.nav

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.auth.ui.screens.signin.SignInScreen
import dev.bittim.valolink.auth.ui.screens.signin.SignInViewModel
import kotlinx.serialization.Serializable

@Serializable
object SignInNav

fun NavGraphBuilder.signInScreen(
    onNavToContent: () -> Unit,
    onNavToSignUp: () -> Unit,
) {
    composable<SignInNav> {
        val viewModel: SignInViewModel = hiltViewModel()
        val signInState by viewModel.state.collectAsStateWithLifecycle()

        SignInScreen(
            state = signInState,
            snackbarHostState = viewModel.snackbarHostState,

            onEmailValueChange = viewModel::onEmailChange,
            onPasswordValueChange = viewModel::onPasswordChange,
            onForgotEmailValueChange = viewModel::onForgotEmailChange,
            onForgotDismissRequest = viewModel::onForgotDismiss,
            onForgotConfirmation = viewModel::onForgotConfirmation,
            onSignInClicked = viewModel::onSignInClicked,
            onForgotClicked = viewModel::onForgotClicked,
            onNavContent = onNavToContent,
            onNavSignUp = onNavToSignUp
        )
    }
}

fun NavController.navToSignIn() {
    navigate(SignInNav) {
        launchSingleTop = true
    }
}