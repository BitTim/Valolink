/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SigninNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.12.24, 17:29
 */

package dev.bittim.valolink.onboarding.ui.screens.signin

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SigninNav

fun NavGraphBuilder.signinScreen(
    onNavBack: () -> Unit,
    onNavToPasswordForgot: () -> Unit,
    onNavToCreateAccount: () -> Unit,
) {
    composable<SigninNav> {
        val viewModel: SigninViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        SigninScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            onForgotPassword = onNavToPasswordForgot,
            onCreateAccount = onNavToCreateAccount,
            onCancel = onNavBack,
            onContinue = { }
        )
    }
}