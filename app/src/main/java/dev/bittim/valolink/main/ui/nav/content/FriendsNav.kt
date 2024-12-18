/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       FriendsNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   18.12.24, 02:29
 */

package dev.bittim.valolink.main.ui.nav.content

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.main.ui.screens.content.friends.FriendsScreen
import dev.bittim.valolink.main.ui.screens.content.friends.FriendsViewModel
import kotlinx.serialization.Serializable

@Serializable
object FriendsNav

fun NavGraphBuilder.friendsScreen() {
    composable<FriendsNav> {
        val viewModel: FriendsViewModel = hiltViewModel()
        val friendsState by viewModel.friendsState.collectAsStateWithLifecycle()

        FriendsScreen(
            state = friendsState,
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