package dev.bittim.valolink.feature.content.ui.contracts.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun AgentCardBase(
    modifier: Modifier = Modifier,
    backgroundGradientColors: List<String>,
    backgroundImage: String?,
    isDisabled: Boolean = false,
    content: @Composable (Boolean) -> Unit
) {
    val useGradient = backgroundGradientColors.isNotEmpty() && backgroundGradientColors.count() >= 4

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = if (useGradient) CardDefaults.cardColors()
            .copy(contentColor = Color.White) else CardDefaults.cardColors()
    ) {
        AgentBackdrop(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            useGradient = useGradient,
            isDisabled = isDisabled,
            backgroundGradientColors = backgroundGradientColors,
            backgroundImage = backgroundImage
        ) {
            content(useGradient)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AgentCardBaseHorizontalPreview() {
    ValolinkTheme {
        AgentCardBase(
            backgroundGradientColors = listOf(
                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
        ) {
            Box(Modifier.height(100.dp)) {}
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AgentCardBaseVerticalPreview() {
    ValolinkTheme {
        AgentCardBase(
            Modifier.width(100.dp),
            backgroundGradientColors = listOf(
                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
        ) {
            Box(Modifier.height(300.dp)) {}
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DisabledAgentCardBaseHorizontalPreview() {
    ValolinkTheme {
        AgentCardBase(
            backgroundGradientColors = listOf(
                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
            isDisabled = true
        ) {
            Box(Modifier.height(100.dp)) {}
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DisabledAgentCardBaseVerticalPreview() {
    ValolinkTheme {
        AgentCardBase(
            Modifier.width(100.dp),
            backgroundGradientColors = listOf(
                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
            isDisabled = true
        ) {
            Box(Modifier.height(300.dp)) {}
        }
    }
}