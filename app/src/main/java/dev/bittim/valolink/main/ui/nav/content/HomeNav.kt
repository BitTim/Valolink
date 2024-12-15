/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       HomeNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
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
import dev.bittim.valolink.main.ui.screens.content.home.HomeScreen
import dev.bittim.valolink.main.ui.screens.content.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
object HomeNav

fun NavGraphBuilder.homeScreen(
    onSignOutClicked: () -> Unit,
) {
    composable<HomeNav>(enterTransition = { Transition.topLevelEnter },
                        exitTransition = { Transition.topLevelExit },
                        popEnterTransition = { Transition.topLevelEnter },
                        popExitTransition = { Transition.topLevelExit }) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = homeState,
            onSignOutClicked = onSignOutClicked
        )
    }
}

fun NavController.navToHome() {
    navigate(HomeNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}