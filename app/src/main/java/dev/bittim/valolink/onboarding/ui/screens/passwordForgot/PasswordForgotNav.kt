/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PasswordForgotNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.12.24, 17:28
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordForgot

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PasswordForgotNav

fun NavGraphBuilder.passwordForgotScreen(
    onNavBack: () -> Unit
) {
    composable<PasswordForgotNav> {
        val viewModel: PasswordForgotViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        PasswordForgotScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            onCancel = onNavBack,
            onContinue = {}
        )
    }
}