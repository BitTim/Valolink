/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CoilDebugPlaceholder.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.content.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource

@Composable
fun coilDebugPlaceholder(@DrawableRes debugPreview: Int, visible: Boolean = true) =
    if (LocalInspectionMode.current && visible) {
        painterResource(id = debugPreview)
    } else {
        null
    }
