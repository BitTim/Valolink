/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AppScaffold.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 17:13
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.nav.AuthenticatedDestination
import dev.bittim.valolink.core.nav.NavBarDestination
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.iconcd_add

@Composable
fun AppScaffold(
    showNavBar: Boolean = false,
    onNavigateTopLevel: (AuthenticatedDestination) -> Unit,
    onFabClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var currentDestinationIndex by rememberSaveable { mutableIntStateOf(NavBarDestination.Home.ordinal) }

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
        floatingActionButton = {
            AnimatedVisibility(visible = showNavBar) {
                FloatingActionButton(
                    onClick = { onFabClick() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.iconcd_add))
                }
            }
        },
        content = content
    )
}

@Composable
@Preview
fun AppScaffoldPreview() {
    MaterialTheme {
        Surface {
            AppScaffold(
                showNavBar = true,
                onNavigateTopLevel = {},
                onFabClick = {},
                content = { padding ->
                    Text("Content", modifier = Modifier.padding(padding))
                }
            )
        }
    }
}