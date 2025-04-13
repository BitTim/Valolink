/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:54
 */

package dev.bittim.valolink.content.ui.container

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.bittim.valolink.content.ui.container.components.navbar.NavItem
import dev.bittim.valolink.content.ui.screens.content.ContentNavGraph

@Composable
fun ContentContainerScreen(
    state: ContentContainerState,
    navController: NavHostController,
    navOnboarding: () -> Unit,
    onSignOutClicked: () -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(NavItem.Home) }

    // TODO: Rework with local accounts
    if (state.isAuthenticated != null && !state.isAuthenticated) {
        LaunchedEffect(Unit) {
            navOnboarding()
        }
    } else {
        if (state.hasOnboarded != null && !state.hasOnboarded) {
            LaunchedEffect(Unit) {
                navOnboarding()
            }
        }
    }

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems = {
            NavItem.entries.forEach {
                val isSelected = it == currentDestination

                item(
                    icon = if (isSelected) it.activeIcon else it.inactiveIcon,
                    label = { Text(it.title) },
                    selected = isSelected,
                    onClick = {
                        currentDestination = it
                        it.navigation(navController)
                    })
            }
        }) {
        ContentNavGraph(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            onSignOutClicked = onSignOutClicked
        )
    }
}
