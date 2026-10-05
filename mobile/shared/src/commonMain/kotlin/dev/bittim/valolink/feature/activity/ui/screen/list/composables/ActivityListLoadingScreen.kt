/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListLoadingScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   26.09.26, 15:55
 */

package dev.bittim.valolink.feature.activity.ui.screen.list.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.components.SeamlessLazyColumn
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardSkeleton

@Composable
fun ActivityListLoadingScreen(
    modifier: Modifier = Modifier,
    placeholderCount: Int = 10
) {
    SeamlessLazyColumn(
        modifier = modifier,
    ) {
        items(placeholderCount) {
            MatchCardSkeleton(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
@Preview
fun ActivityListLoadingScreenPreview() {
    MaterialTheme {
        Surface {
            ActivityListLoadingScreen()
        }
    }
}