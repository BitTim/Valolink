/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FinishNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:28
 */

package dev.bittim.valolink.onboarding.ui.screens.finish

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object FinishNav

fun NavGraphBuilder.finishScreen() {
    composable<FinishNav> {
        val viewModel: FinishViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        FinishScreen(
            state = state.value,
            navBack = viewModel::navBack,
            finish = viewModel::finish
        )
    }
}

fun NavController.navToOnboardingFinish() {
    navigate(FinishNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = false
        }
        launchSingleTop = true
        restoreState = false
    }
}
