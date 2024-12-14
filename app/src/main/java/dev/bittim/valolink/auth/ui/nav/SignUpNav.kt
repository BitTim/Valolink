/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SignUpNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.auth.ui.nav

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.auth.ui.screens.signup.SignUpScreen
import dev.bittim.valolink.auth.ui.screens.signup.SignUpViewModel
import kotlinx.serialization.Serializable

@Serializable
object SignUpNav

fun NavGraphBuilder.signUpScreen(
    onNavToContent: () -> Unit,
    onNavToSignIn: () -> Unit,
) {
    composable<SignUpNav> {
        val viewModel: SignUpViewModel = hiltViewModel()
        val signUpState by viewModel.state.collectAsStateWithLifecycle()

        SignUpScreen(
            signUpState,
            onEmailValueChange = viewModel::onEmailChange,
            onUsernameValueChange = viewModel::onUsernameChange,
            onPasswordValueChange = viewModel::onPasswordChange,
            onConfirmPasswordValueChange = viewModel::onConfirmPasswordChange,
            onSignUpClicked = viewModel::onSignUpClicked,
            onNavToContent = onNavToContent,
            onNavToSignIn = onNavToSignIn,
        )
    }
}

fun NavController.navToSignUp() {
    navigate(SignUpNav) {
        launchSingleTop = true
    }
}