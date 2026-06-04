/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NavBarDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 01:19
 */

package dev.bittim.valolink.core.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import dev.bittim.valolink.feature.activity.nav.ActivityListScreenNav
import dev.bittim.valolink.feature.home.nav.HomeScreenNav

enum class NavBarDestination(
    val title: String,
    val icon: ImageVector,
    val destination: AuthenticatedDestination
) {
    Home (
        title = "Home",
        icon = Icons.Default.Home,
        destination = HomeScreenNav
    ),

    Activity (
        title = "Activity",
        icon = Icons.Default.History,
        destination = ActivityListScreenNav
    ),
}