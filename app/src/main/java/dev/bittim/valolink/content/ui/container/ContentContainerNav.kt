/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:28
 */

package dev.bittim.valolink.content.ui.container

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object ContentContainerNav

fun NavGraphBuilder.content(
    navOnboarding: () -> Unit,
) {
    composable<ContentContainerNav> {
        val viewModel: ContentContainerViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val subNavController = rememberNavController()

        ContentContainerScreen(
            state = state,
            navController = subNavController,
            navOnboarding = navOnboarding,
            onSignOutClicked = viewModel::onSignOutClicked
        )
    }
}

fun NavController.navToContent() {
    navigate(ContentContainerNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
