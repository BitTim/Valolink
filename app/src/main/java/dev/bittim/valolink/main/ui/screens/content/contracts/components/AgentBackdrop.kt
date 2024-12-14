/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AgentBackdrop.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.screens.content.contracts.components

import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional
import dev.bittim.valolink.main.ui.util.parseAndSaturateColor
import org.intellij.lang.annotations.Language

@Language("AGSL")
val AGENT_GRADIENT_SHADER = """
    uniform float2 resolution;
    layout(color) uniform half4 color1;
    layout(color) uniform half4 color2;
    layout(color) uniform half4 color3;
    layout(color) uniform half4 color4;

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord/resolution.xy;
        return mix(mix(mix(color1, color4, uv.y),  mix(color2, color3, uv.y), uv.x), half4(0, 0, 0, 1), 0.3);
    }
""".trimIndent()

@Composable
fun AgentBackdrop(
    modifier: Modifier = Modifier,
    useGradient: Boolean,
    isDisabled: Boolean,
    backgroundGradientColors: List<String>,
    backgroundImage: String?,
    content: @Composable (Boolean) -> Unit,
) {
    Box(modifier = modifier
        .background(Color.Transparent)
        .conditional(useGradient) {
            drawWithCache {
                val shader = RuntimeShader(AGENT_GRADIENT_SHADER)
                val shaderBrush = ShaderBrush(shader)
                shader.setFloatUniform(
                    "resolution",
                    size.width,
                    size.height
                )
                onDrawBehind {
                    shader.setColorUniform(
                        "color1",
                        parseAndSaturateColor(
                            "#" + backgroundGradientColors[0].substring(6) + backgroundGradientColors[0].substring(
                                0,
                                6
                            ),
                            if (isDisabled) 0.3f else 1f
                        )
                    )
                    shader.setColorUniform(
                        "color2",
                        parseAndSaturateColor(
                            "#" + backgroundGradientColors[1].substring(6) + backgroundGradientColors[1].substring(
                                0,
                                6
                            ),
                            if (isDisabled) 0.3f else 1f
                        )
                    )
                    shader.setColorUniform(
                        "color3",
                        parseAndSaturateColor(
                            "#" + backgroundGradientColors[2].substring(6) + backgroundGradientColors[2].substring(
                                0,
                                6
                            ),
                            if (isDisabled) 0.3f else 1f
                        )
                    )
                    shader.setColorUniform(
                        "color4",
                        parseAndSaturateColor(
                            "#" + backgroundGradientColors[3].substring(6) + backgroundGradientColors[3].substring(
                                0,
                                6
                            ),
                            if (isDisabled) 0.3f else 1f
                        )
                    )
                    drawRect(shaderBrush)
                }
            }
        }) {
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

        content(useGradient)
    }
}