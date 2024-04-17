package dev.bittim.valolink.feature.content.ui.contracts.components

import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.feature.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.feature.content.ui.components.conditional
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
fun AgentCardBase(
    modifier: Modifier = Modifier,
    backgroundGradientColors: List<String>,
    backgroundImage: String?,
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
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color.Transparent)
            .conditional(useGradient) {
                drawWithCache {
                    val shader = RuntimeShader(AGENT_GRADIENT_SHADER)
                    val shaderBrush = ShaderBrush(shader)
                    shader.setFloatUniform("resolution", size.width, size.height)
                    onDrawBehind {
                        shader.setColorUniform(
                            "color1", android.graphics.Color.parseColor(
                                "#" + backgroundGradientColors[0].substring(6) + backgroundGradientColors[0].substring(
                                    0, 6
                                )
                            )
                        )
                        shader.setColorUniform(
                            "color2", android.graphics.Color.parseColor(
                                "#" + backgroundGradientColors[1].substring(6) + backgroundGradientColors[1].substring(
                                    0, 6
                                )
                            )
                        )
                        shader.setColorUniform(
                            "color3", android.graphics.Color.parseColor(
                                "#" + backgroundGradientColors[2].substring(6) + backgroundGradientColors[2].substring(
                                    0, 6
                                )
                            )
                        )
                        shader.setColorUniform(
                            "color4", android.graphics.Color.parseColor(
                                "#" + backgroundGradientColors[3].substring(6) + backgroundGradientColors[3].substring(
                                    0, 6
                                )
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
                        .fillMaxHeight(),

                    model = backgroundImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    placeholder = coilDebugPlaceholder(R.drawable.debug_agent_background_image)
                )
            }

            content(useGradient)
        }
    }
}