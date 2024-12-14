/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       MatchesNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.nav.content

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.matches.MatchesScreen
import dev.bittim.valolink.main.ui.screens.content.matches.MatchesViewModel
import kotlinx.serialization.Serializable

@Serializable
object MatchesNav

fun NavGraphBuilder.matchesScreen() {
    composable<MatchesNav>(enterTransition = { Transition.topLevelEnter },
                           exitTransition = { Transition.topLevelExit },
                           popEnterTransition = { Transition.topLevelEnter },
                           popExitTransition = { Transition.topLevelExit }) {
        val viewModel: MatchesViewModel = hiltViewModel()
        val matchesState by viewModel.matchesState.collectAsStateWithLifecycle()

        MatchesScreen(
            state = matchesState,
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