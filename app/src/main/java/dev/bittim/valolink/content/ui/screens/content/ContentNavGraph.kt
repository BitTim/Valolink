/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:37
 */

package dev.bittim.valolink.content.ui.screens.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.content.ui.screens.content.contracts.contractsNavGraph
import dev.bittim.valolink.content.ui.screens.content.friends.friendsScreen
import dev.bittim.valolink.content.ui.screens.content.home.HomeNav
import dev.bittim.valolink.content.ui.screens.content.home.homeScreen
import dev.bittim.valolink.content.ui.screens.content.matches.matchesScreen
import dev.bittim.valolink.core.ui.theme.Transition

@Composable
fun ContentNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    onSignOutClicked: () -> Unit,
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
        homeScreen(onSignOutClicked = onSignOutClicked)
        contractsNavGraph(navController)
        matchesScreen()
        friendsScreen()
    }
}
