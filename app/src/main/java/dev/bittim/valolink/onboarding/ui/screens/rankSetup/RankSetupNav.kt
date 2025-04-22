/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 22:59
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object RankSetupNav

fun NavGraphBuilder.rankSetupScreen() {
    composable<RankSetupNav> {
        val viewModel: RankSetupViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        RankSetupScreen(
            state = state.value,
            navBack = viewModel::navBack,
            setRank = {
                viewModel.setRank(
                    state.value.tier,
                    state.value.rr,
                    state.value.matchesPlayed,
                    state.value.matchesNeeded
                )
            },
            onTierChanged = viewModel::onTierChanged,
            onRRChanged = viewModel::onRRChanged,
            onMatchesPlayedChanged = viewModel::onMatchesPlayedChanged,
            onMatchesNeededChanged = viewModel::onMatchesNeededChanged
        )
    }
}

fun NavController.navOnboardingRankSetup() {
    navigate(RankSetupNav) {
        launchSingleTop = true
        restoreState = true
    }
}
