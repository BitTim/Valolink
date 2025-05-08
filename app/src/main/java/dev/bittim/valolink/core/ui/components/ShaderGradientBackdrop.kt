/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ShaderGradientBackdrop.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.04.25, 17:30
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.color.ShaderGradient
import dev.bittim.valolink.core.ui.util.color.drawShaderGradient
import dev.bittim.valolink.core.ui.util.extensions.modifier.conditional

@Composable
fun ShaderGradientBackdrop(
    modifier: Modifier = Modifier,
    isDisabled: Boolean,
    gradient: ShaderGradient?,
    backgroundImage: String? = null,
    content: @Composable (Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .conditional(gradient != null) {
                drawShaderGradient(gradient!!, isDisabled)
            }
    ) {
        if (backgroundImage != null) {
            AsyncImage(
                modifier = Modifier
                    .alpha(0.15f)
                    .fillMaxSize()
                    .blur(4.dp),
                model = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                placeholder = coilDebugPlaceholder(R.drawable.debug_agent_background_image)
            )
        }

        content(gradient != null)
    }
}

@ComponentPreviewAnnotations
@Composable
fun AgentBackdropPreview() {
    ValolinkTheme {
        Surface {
            Column {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp)
                ) {
                    ShaderGradientBackdrop(
                        isDisabled = false,
                        gradient = ShaderGradient(
                            "f17cadff",
                            "062261ff",
                            "c347c7ff",
                            "f1db6fff"
                        ),
                        backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png"
                    ) { }
                }

                Spacer(modifier = Modifier.height(Spacing.s))


                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp)
                ) {
                    ShaderGradientBackdrop(
                        isDisabled = false,
                        gradient = ShaderGradient(
                            "4f514fff",
                            "828282ff",
                        ),
                        backgroundImage = null
                    ) { }
                }
            }
        }
    }
}
