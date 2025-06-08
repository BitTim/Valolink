/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ToggleIcon.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 17:49
 */

package dev.bittim.valolink.core.ui.util

import androidx.compose.ui.graphics.vector.ImageVector

data class ToggleIcon(
    val active: ImageVector,
    val inactive: ImageVector,
    val contentDescription: UiText?,
)
