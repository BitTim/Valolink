/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OrientableContainer.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.core.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

/**
 * A composable container that detects orientation and decides which of the provided containers to use based on the detected orientation
 * @param modifier The modifier to apply to the chosen container
 * @param portraitContainer The container to use in portrait mode. A reference to the modifier and the content of the container is provided.
 * @param landscapeContainer The container to use in landscape mode. A reference to the modifier and the content of the container is provided.
 * @param content The content of the container. The orientation is provided as a parameter.
 */
@Composable
fun OrientableContainer(
    modifier: Modifier = Modifier,
    portraitContainer: @Composable (modifier: Modifier, content: @Composable () -> Unit) -> Unit,
    landscapeContainer: @Composable (modifier: Modifier, content: @Composable () -> Unit) -> Unit,
    content: @Composable (orientation: Int) -> Unit
) {
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            landscapeContainer(modifier) {
                content(orientation)
            }
        }

        else -> {
            portraitContainer(modifier) {
                content(orientation)
            }
        }
    }
}