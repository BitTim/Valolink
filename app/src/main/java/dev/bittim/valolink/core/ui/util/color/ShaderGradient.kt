/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ShaderGradient.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.04.25, 14:53
 */

package dev.bittim.valolink.core.ui.util.color

import android.graphics.RuntimeShader
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import org.intellij.lang.annotations.Language

@Language("AGSL")
val GRADIENT_SHADER = """
    uniform float2 resolution;
    layout(color) uniform half4 colorTopLeft;
    layout(color) uniform half4 colorTopRight;
    layout(color) uniform half4 colorBottomRight;
    layout(color) uniform half4 colorBottomLeft;

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord/resolution.xy;
        return mix(mix(mix(colorTopLeft, colorBottomLeft, uv.y),  mix(colorTopRight, colorBottomRight, uv.y), uv.x), half4(0, 0, 0, 1), 0.15);
    }
""".trimIndent()

class ShaderGradient() {
    private var argbTopLeft: String = ""
    private var argbTopRight: String = ""
    private var argbBottomRight: String = ""
    private var argbBottomLeft: String = ""
    private var disabledSaturation: Float = 0f
    private var enabledSaturation: Float = 1f

    companion object {
        fun fromList(list: List<String>): ShaderGradient? {
            return when (list.size) {
                1 -> ShaderGradient(list[0])
                2 -> ShaderGradient(list[0], list[1])
                4 -> ShaderGradient(list[0], list[1], list[2], list[3])
                else -> null
            }
        }
    }

    constructor(
        rgba: String,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = convertRGBAtoARGB(rgba, true)
        this.argbTopRight = convertRGBAtoARGB(rgba, true)
        this.argbBottomRight = convertRGBAtoARGB(rgba, true)
        this.argbBottomLeft = convertRGBAtoARGB(rgba, true)
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        rgbaTopLeftBottomRight: String,
        rgbaTopRightBottomLeft: String,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = convertRGBAtoARGB(rgbaTopLeftBottomRight, true)
        this.argbTopRight = convertRGBAtoARGB(rgbaTopRightBottomLeft, true)
        this.argbBottomRight = convertRGBAtoARGB(rgbaTopLeftBottomRight, true)
        this.argbBottomLeft = convertRGBAtoARGB(rgbaTopRightBottomLeft, true)
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        rgbaTopLeft: String,
        rgbaTopRight: String,
        rgbaBottomRight: String,
        rgbaBottomLeft: String,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = convertRGBAtoARGB(rgbaTopLeft, true)
        this.argbTopRight = convertRGBAtoARGB(rgbaTopRight, true)
        this.argbBottomLeft = convertRGBAtoARGB(rgbaBottomLeft, true)
        this.argbBottomRight = convertRGBAtoARGB(rgbaBottomRight, true)
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    fun getColorTopLeft(isDisabled: Boolean): Int {
        return parseAndSaturateColor(
            argbTopLeft,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }

    fun getColorTopRight(isDisabled: Boolean): Int {
        return parseAndSaturateColor(
            argbTopRight,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }

    fun getColorBottomRight(isDisabled: Boolean): Int {
        return parseAndSaturateColor(
            argbBottomRight,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }

    fun getColorBottomLeft(isDisabled: Boolean): Int {
        return parseAndSaturateColor(
            argbBottomLeft,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }
}

fun Modifier.drawShaderGradient(gradient: ShaderGradient, isDisabled: Boolean): Modifier {
    return this.then(
        Modifier.drawWithCache {
            val shader = RuntimeShader(GRADIENT_SHADER)
            val shaderBrush = ShaderBrush(shader)
            shader.setFloatUniform(
                "resolution",
                size.width,
                size.height
            )
            onDrawBehind {
                shader.setColorUniform(
                    "colorTopLeft",
                    gradient.getColorTopLeft(isDisabled)
                )
                shader.setColorUniform(
                    "colorTopRight",
                    gradient.getColorTopRight(isDisabled)
                )
                shader.setColorUniform(
                    "colorBottomRight",
                    gradient.getColorBottomRight(isDisabled)
                )
                shader.setColorUniform(
                    "colorBottomLeft",
                    gradient.getColorBottomLeft(isDisabled)
                )
                drawRect(shaderBrush)
            }
        }
    )
}
