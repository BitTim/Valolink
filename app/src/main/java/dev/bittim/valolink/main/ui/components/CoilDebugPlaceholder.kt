/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CoilDebugPlaceholder.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.components

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