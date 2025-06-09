/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content

import android.graphics.Bitmap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.content.ui.screens.content.contracts.contractsNavGraph
import dev.bittim.valolink.content.ui.screens.content.friends.friendsScreen
import dev.bittim.valolink.content.ui.screens.content.home.HomeNav
import dev.bittim.valolink.content.ui.screens.content.home.homeScreen
import dev.bittim.valolink.content.ui.screens.content.matches.matchesNavGraph
import dev.bittim.valolink.core.ui.theme.Transition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    userAvatar: Bitmap?,
    signOut: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeNav,
        enterTransition = { Transition.TopLevel.enter },
        exitTransition = { Transition.TopLevel.exit },
        popEnterTransition = { Transition.TopLevel.popEnter },
        popExitTransition = { Transition.TopLevel.popExit }
    ) {
        homeScreen(signOut = signOut, userAvatar = userAvatar)
        contractsNavGraph(navController = navController, userAvatar = userAvatar)
        matchesNavGraph(navController = navController, userAvatar = userAvatar)
        friendsScreen(userAvatar = userAvatar)
    }
}
