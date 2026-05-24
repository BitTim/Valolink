/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NavBarDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 02:53
 */

package dev.bittim.valolink.core.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import dev.bittim.valolink.feature.home.nav.HomeScreenNav

enum class NavBarDestination(
    val title: String,
    val icon: ImageVector,
    val destination: AppDestination
) {
    HOME (
        title = "Home",
        icon = Icons.Default.Home,
        destination = HomeScreenNav
    )
}