/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ContractSetupNav

fun NavGraphBuilder.contractSetupScreen() {
    composable<ContractSetupNav> {
        val viewModel = hiltViewModel<ContractSetupViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()

        ContractSetupScreen(
            state = state.value
        )
    }
}

fun NavController.navOnboardingContractSetup() {
    navigate(ContractSetupNav) {
        launchSingleTop = true
        restoreState = true
    }
}
