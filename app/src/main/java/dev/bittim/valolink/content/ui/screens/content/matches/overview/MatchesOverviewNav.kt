/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesOverviewNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.matches.overview

import android.graphics.Bitmap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object MatchesOverviewNav

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.matchesOverviewScreen(
    userAvatar: Bitmap?
) {
    composable<MatchesOverviewNav> {
        val viewModel: MatchesOverviewViewModel = hiltViewModel()
        val matchesState by viewModel.state.collectAsStateWithLifecycle()

        MatchesScreen(
            state = matchesState,
            userAvatar = userAvatar,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToMatches() {
    navigate(MatchesOverviewNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
