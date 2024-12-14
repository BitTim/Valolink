/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       MainNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.auth.ui.nav.navToAuthGraph
import dev.bittim.valolink.main.ui.nav.content.ContentContainerNav
import dev.bittim.valolink.main.ui.nav.content.contentContainerScreen
import dev.bittim.valolink.main.ui.nav.onboarding.navToOnboardingGraph
import dev.bittim.valolink.main.ui.nav.onboarding.onboardingNavGraph
import kotlinx.serialization.Serializable

@Serializable
object MainNavGraph

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
) {
    navigation<MainNavGraph>(
        startDestination = ContentContainerNav
    ) {
        contentContainerScreen(navToAuthGraph = { navController.navToAuthGraph() },
                               navToOnboardingGraph = { navController.navToOnboardingGraph() })
        onboardingNavGraph(navController)
    }
}

fun NavController.navToMainGraph() {
    navigate(MainNavGraph) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}