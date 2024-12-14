/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.nav.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.nav.content.contracts.contractsNavGraph

@Composable
fun ContentNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    onSignOutClicked: () -> Unit,
) {
    NavHost(modifier = modifier,
            navController = navController,
            startDestination = HomeNav,
            enterTransition = { Transition.topLevelEnter },
            exitTransition = { Transition.topLevelExit },
            popEnterTransition = { Transition.topLevelEnter },
            popExitTransition = { Transition.topLevelExit }) {
        homeScreen(onSignOutClicked = onSignOutClicked)
        contractsNavGraph(navController)
        matchesScreen()
        friendsScreen()
    }
}