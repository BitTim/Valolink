package dev.bittim.valolink.main.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

fun Modifier.pulseAnimation(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "Loading pulse")
    val color by transition.animateColor(
        initialValue = MaterialTheme.colorScheme.surfaceContainerLow,
        targetValue = MaterialTheme.colorScheme.surfaceContainerHigh,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse,
        ), label = "Pulse animation"
    )

    drawBehind { drawRect(color = color) }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPulseAnimation() {
    ValolinkTheme {
        Column {
            Row {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .pulseAnimation()
                )

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .pulseAnimation()
                )
            }

            Row {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .pulseAnimation()
                )

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .pulseAnimation()
                )
            }
        }

    }
}