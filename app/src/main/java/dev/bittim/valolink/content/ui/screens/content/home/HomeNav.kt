/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HomeNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.content.ui.screens.content.home

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HomeNav

fun NavGraphBuilder.homeScreen(
    signOut: () -> Unit,
) {
    composable<HomeNav> {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = homeState,
            signOut = signOut
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
