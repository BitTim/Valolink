/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.nav.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.main.ui.nav.content.navToContentContainer
import kotlinx.serialization.Serializable

@Serializable
object OnboardingNavGraph

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController,
) {
    navigation<OnboardingNavGraph>(
        startDestination = GetStartedNav,
    ) {
        getStartedScreen(onNavToContent = { navController.navToContentContainer() })
    }
}

fun NavController.navToOnboardingGraph() {
    navigate(OnboardingNavGraph) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}