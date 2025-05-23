/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentCardBase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.04.25, 14:53
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.components

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
import dev.bittim.valolink.core.ui.components.ShaderGradientBackdrop
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.color.ShaderGradient

@Composable
fun AgentCardBase(
    modifier: Modifier = Modifier,
    gradient: ShaderGradient?,
    backgroundImage: String?,
    isDisabled: Boolean = false,
    content: @Composable (Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = if (gradient != null) CardDefaults
            .cardColors()
            .copy(contentColor = Color.White) else CardDefaults.cardColors()
    ) {
        ShaderGradientBackdrop(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            isDisabled = isDisabled,
            gradient = gradient,
            backgroundImage = backgroundImage
        ) {
            content(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgentCardBaseHorizontalPreview() {
    ValolinkTheme {
        AgentCardBase(
            gradient = ShaderGradient(
                "f17cadff",
                "062261ff",
                "c347c7ff",
                "f1db6fff"
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
            gradient = ShaderGradient(
                "f17cadff",
                "062261ff",
                "c347c7ff",
                "f1db6fff"
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
            gradient = ShaderGradient(
                "f17cadff",
                "062261ff",
                "c347c7ff",
                "f1db6fff"
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
            gradient = ShaderGradient(
                "f17cadff",
                "062261ff",
                "c347c7ff",
                "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
            isDisabled = true
        ) {
            Box(Modifier.height(300.dp)) {}
        }
    }
}
