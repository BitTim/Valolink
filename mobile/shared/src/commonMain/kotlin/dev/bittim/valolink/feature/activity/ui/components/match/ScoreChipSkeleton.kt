/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ScoreChipSkeleton.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   05.10.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.components.match

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.extensions.pulseAnimation

@Composable
fun ScoreChipSkeleton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(ScoreChipDefaults.height)
            .width(ScoreChipDefaults.skeletonWidth)
            .clip(RoundedCornerShape(ScoreChipDefaults.cornerRadius))
            .pulseAnimation()
    )
}

@Composable
@Preview
fun ScoreChipSkeletonPreview() {
    MaterialTheme {
        Surface {
            ScoreChipSkeleton()
        }
    }
}