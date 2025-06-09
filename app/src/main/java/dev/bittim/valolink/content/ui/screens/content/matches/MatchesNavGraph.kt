/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.matches

import android.graphics.Bitmap
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.content.ui.screens.content.matches.overview.MatchesOverviewNav
import dev.bittim.valolink.content.ui.screens.content.matches.overview.matchesOverviewScreen
import kotlinx.serialization.Serializable

@Serializable
object MatchesNav

fun NavGraphBuilder.matchesNavGraph(
    navController: NavController,
    userAvatar: Bitmap?,
) {
    navigation<MatchesNav>(
        startDestination = MatchesOverviewNav
    ) {
        matchesOverviewScreen(
            userAvatar = userAvatar
        )
    }
}

fun NavController.navToMatchesGraph() {
    navigate(MatchesNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}