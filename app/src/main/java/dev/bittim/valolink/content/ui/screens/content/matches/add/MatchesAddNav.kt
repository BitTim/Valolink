/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesAddNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 22:18
 */

package dev.bittim.valolink.content.ui.screens.content.matches.add

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object MatchesAddNav

fun NavGraphBuilder.matchesAddScreen(
    onNavBack: () -> Unit
) {
    composable<MatchesAddNav> {
        val viewModel: MatchesAddViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MatchesAddScreen(
            state = state,
            determineScoreResult = viewModel::determineScoreResult,
            determineRankChangeResult = viewModel::determineRankChangeResult,
            onNavBack = onNavBack
        )
    }
}

fun NavController.navToMatchesAdd() {
    navigate(MatchesAddNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}