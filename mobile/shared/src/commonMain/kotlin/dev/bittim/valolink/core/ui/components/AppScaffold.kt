/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AppScaffold.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:37
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.bittim.valolink.core.nav.AuthenticatedDestination
import dev.bittim.valolink.core.nav.NavBarDestination

@Composable
fun AppScaffold(
    showNavBar: Boolean = false,
    onNavigateTopLevel: (AuthenticatedDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var currentDestinationIndex by rememberSaveable { mutableIntStateOf(NavBarDestination.HOME.ordinal) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(visible = showNavBar) {
                NavigationBar {
                    NavBarDestination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            label = { Text(destination.title) },
                            icon = { Icon(destination.icon, contentDescription = null) },
                            selected = index == currentDestinationIndex,
                            onClick = {
                                onNavigateTopLevel(destination.destination)
                                currentDestinationIndex = index
                            }
                        )
                    }
                }
            }
        },
        content = content
    )
}