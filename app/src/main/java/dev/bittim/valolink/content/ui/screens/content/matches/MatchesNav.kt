/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.matches

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
object MatchesNav

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.matchesScreen(
    userAvatar: Bitmap?
) {
    composable<MatchesNav> {
        val viewModel: MatchesViewModel = hiltViewModel()
        val matchesState by viewModel.state.collectAsStateWithLifecycle()

        MatchesScreen(
            state = matchesState,
            userAvatar = userAvatar,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToMatches() {
    navigate(MatchesNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
