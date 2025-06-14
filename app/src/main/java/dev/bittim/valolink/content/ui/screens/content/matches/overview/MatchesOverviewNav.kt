/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesOverviewNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.06.25, 02:07
 */

package dev.bittim.valolink.content.ui.screens.content.matches.overview

import android.graphics.Bitmap
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

fun NavGraphBuilder.matchesOverviewScreen(
    userAvatar: Bitmap?,
    navToMatchesAdd: () -> Unit,
) {
    composable<MatchesOverviewNav> {
        val viewModel: MatchesOverviewViewModel = hiltViewModel()
        val matchesState by viewModel.state.collectAsStateWithLifecycle()

        MatchesOverviewScreen(
            state = matchesState,
            userAvatar = userAvatar,
            onFetch = viewModel::onFetch,
            navToMatchesAdd = navToMatchesAdd
        )
    }
}

fun NavController.navToMatchesOverview() {
    navigate(MatchesOverviewNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
