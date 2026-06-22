/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SeamlessLazyColumn.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 19:30
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dev.bittim.valolink.core.ui.Spacing

@Composable
fun SeamlessLazyColumn(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
            content = content
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut(),
            visible = lazyListState.canScrollBackward
        ) {
            Box(
                modifier = Modifier.aspectRatio(5f/1f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut(),
            visible = lazyListState.canScrollForward
        ) {
            Box(
                modifier = Modifier.aspectRatio(5f/1f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            )
        }
    }
}