/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LevelBackdrop.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.screens.content.contracts.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder

@Composable
fun LevelBackdrop(
    modifier: Modifier = Modifier,
    isDisabled: Boolean,
    backgroundImage: String?,
    content: @Composable () -> Unit,
) {
    if (backgroundImage != null) {
        AsyncImage(
            modifier = modifier
                .fillMaxSize()
                .blur(
                    32.dp
                ),
            model = backgroundImage,
            contentDescription = null,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(if (isDisabled) 0.3f else 0.5f) }),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_displayicon)
        )
    }

    content()
}