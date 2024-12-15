/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RootNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.auth.ui.nav.authNavGraph
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.nav.mainNavGraph
import dev.bittim.valolink.onboarding.ui.OnboardingNavGraph
import dev.bittim.valolink.onboarding.ui.onboardingNavGraph

@Composable
fun RootNavGraph(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = OnboardingNavGraph,
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        onboardingNavGraph(navController = navController)
        authNavGraph(navController = navController)
        mainNavGraph(navController = navController)
    }
}