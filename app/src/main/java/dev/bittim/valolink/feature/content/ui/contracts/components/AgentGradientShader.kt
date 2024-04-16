package dev.bittim.valolink.feature.content.ui.contracts.components

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