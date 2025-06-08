/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FriendsNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.friends

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
object FriendsNav

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.friendsScreen(
    userAvatar: Bitmap?
) {
    composable<FriendsNav> {
        val viewModel: FriendsViewModel = hiltViewModel()
        val friendsState by viewModel.state.collectAsStateWithLifecycle()

        FriendsScreen(
            state = friendsState,
            userAvatar = userAvatar,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToFriends() {
    navigate(FriendsNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
