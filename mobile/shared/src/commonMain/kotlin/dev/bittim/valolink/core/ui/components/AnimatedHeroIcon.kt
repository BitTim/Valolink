/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AnimatedHeroIcon.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 17:33
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.sqrt

enum class SpinDirection {
    Clockwise,
    CounterClockwise,
    None
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedHeroIcon(
    modifier: Modifier = Modifier,
    shape: Shape,
    icon: ImageVector,
    contentDescription: String?,
    spinTrigger: Any,
    spinDirection: SpinDirection,
    spinDurationMillis: Int = 600,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    foregroundColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    var targetRotation by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(spinTrigger) {
        when(spinDirection) {
            SpinDirection.Clockwise -> targetRotation += 360f
            SpinDirection.CounterClockwise -> targetRotation -= 360f
            SpinDirection.None -> {}
        }
    }

    val rotationAngle by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(durationMillis = spinDurationMillis),
        label = "ShapeRotation"
    )

    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
            .graphicsLayer(rotationZ = rotationAngle)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        val diameter = maxWidth
        val safeAreaSize = diameter / sqrt(2f)

        AnimatedContent(
            targetState = icon,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "IconMorph"
        ) { targetIcon ->
            Icon(
                modifier = Modifier.size(safeAreaSize)
                    .graphicsLayer(rotationZ = -rotationAngle),
                imageVector = targetIcon,
                tint = foregroundColor,
                contentDescription = contentDescription
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun AnimatedHeroIconPreview() {
    MaterialTheme {
        Surface {
            AnimatedHeroIcon(
                shape = MaterialShapes.Cookie12Sided.toShape(),
                icon = Icons.Default.Android,
                contentDescription = null,
                spinTrigger = remember { mutableStateOf(Unit) },
                spinDirection = SpinDirection.Clockwise,
            )
        }
    }
}