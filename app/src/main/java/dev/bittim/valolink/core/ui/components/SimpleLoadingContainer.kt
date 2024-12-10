package dev.bittim.valolink.core.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.bittim.valolink.main.ui.components.pulseAnimation

@Composable
fun SimpleLoadingContainer(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    label: String,
    content: @Composable () -> Unit
) {
    Crossfade(
        modifier = modifier,
        targetState = isLoading,
        label = label
    ) {
        if (it) {
            Box(
                modifier = Modifier.clip(MaterialTheme.shapes.large)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pulseAnimation()
                )
            }
        } else {
            content()
        }
    }
}