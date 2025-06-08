/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HomeNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.home

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
object HomeNav

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreen(
    signOut: () -> Unit,
    userAvatar: Bitmap?,
) {
    composable<HomeNav> {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = homeState,
            userAvatar = userAvatar,
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
