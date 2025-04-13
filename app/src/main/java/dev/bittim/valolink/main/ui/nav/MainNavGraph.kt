/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MainNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 14:44
 */

package dev.bittim.valolink.main.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.main.ui.nav.content.ContentContainerNav
import dev.bittim.valolink.main.ui.nav.content.contentContainerScreen
import dev.bittim.valolink.onboarding.ui.container.navToOnboarding
import kotlinx.serialization.Serializable

@Serializable
object MainNavGraph

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
) {
    navigation<MainNavGraph>(
        startDestination = ContentContainerNav
    ) {
        contentContainerScreen(
            navToAuthGraph = { navController.navToOnboarding() },
        )
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
