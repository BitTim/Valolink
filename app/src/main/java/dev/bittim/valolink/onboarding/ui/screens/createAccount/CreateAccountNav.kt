/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CreateAccountNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.onboarding.ui.screens.createAccount

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CreateAccountNav

fun NavGraphBuilder.createAccountScreen(
    onNavBack: () -> Unit
) {
    composable<CreateAccountNav> {
        val viewModel: CreateAccountViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        CreateAccountScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            validatePassword = viewModel::validatePassword,
            onCancel = onNavBack,
            onCreateAccount = {}
        )
    }
}