/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 13:42
 */

package dev.bittim.valolink.content.ui.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.bittim.valolink.content.ui.container.components.navbar.NavItem
import dev.bittim.valolink.content.ui.screens.content.ContentNavGraph
import dev.bittim.valolink.core.ui.theme.Spacing

@Composable
fun ContentContainerScreen(
    state: ContentContainerState,
    navController: NavHostController,
    navOnboarding: () -> Unit,
    signOut: () -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(NavItem.Home) }

    LaunchedEffect(state.isAuthenticated, state.hasOnboarded) {
        if (state.isAuthenticated == false || state.hasOnboarded == false) {
            navOnboarding()
        }
    }

    if (state.isAuthenticated == null || state.hasOnboarded == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(Spacing.m))
            Text("Loading...")
        }
        return
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
            signOut = signOut
        )
    }
}
