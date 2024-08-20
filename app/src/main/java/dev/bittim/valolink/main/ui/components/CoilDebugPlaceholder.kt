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