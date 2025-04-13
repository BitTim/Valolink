/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RootNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:35
 */

package dev.bittim.valolink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.content.ui.container.ContentContainerNav
import dev.bittim.valolink.content.ui.container.content
import dev.bittim.valolink.content.ui.container.navToContent
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.onboarding.ui.container.navToOnboarding
import dev.bittim.valolink.onboarding.ui.container.onboarding

@Composable
fun RootNavGraph(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ContentContainerNav,
        enterTransition = { Transition.TopLevel.enter },
        exitTransition = { Transition.TopLevel.exit },
        popEnterTransition = { Transition.TopLevel.popEnter },
        popExitTransition = { Transition.TopLevel.popExit }
    ) {
        onboarding(navContent = { navController.navToContent() })
        content(navOnboarding = { navController.navToOnboarding() })
    }
}
