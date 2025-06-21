/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ShaderGradient.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 02:20
 */

package dev.bittim.valolink.core.ui.util.color

import android.graphics.RuntimeShader
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
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
        return mix(mix(mix(colorTopLeft, colorBottomLeft, uv.y),  mix(colorTopRight, colorBottomRight, uv.y), uv.x), half4(0, 0, 0, 1), 0.3);
    }
""".trimIndent()

class ShaderGradient() {
    private var argbTopLeft: String = ""
    private var argbTopRight: String = ""
    private var argbBottomRight: String = ""
    private var argbBottomLeft: String = ""
    private var disabledSaturation: Float = 0f
    private var enabledSaturation: Float = 1f

    private fun colorToArgb(color: Color): String {
        return String.format("#%06X", 0xFFFFFF and color.toArgb())
    }

    companion object {
        fun fromARGBList(list: List<String>): ShaderGradient? {
            return when (list.size) {
                1 -> ShaderGradient(list[0])
                2 -> ShaderGradient(list[0], list[1])
                4 -> ShaderGradient(list[0], list[1], list[2], list[3])
                else -> null
            }
        }

        fun fromRGBAList(list: List<String>): ShaderGradient? {
            return when (list.size) {
                1 -> ShaderGradient(convertRGBAtoARGB(list[0], true))
                2 -> ShaderGradient(
                    convertRGBAtoARGB(list[0], true),
                    convertRGBAtoARGB(list[1], true)
                )

                4 -> ShaderGradient(
                    convertRGBAtoARGB(list[0], true),
                    convertRGBAtoARGB(list[1], true),
                    convertRGBAtoARGB(list[2], true),
                    convertRGBAtoARGB(list[3], true)
                )

                else -> null
            }
        }

        fun fromColorList(list: List<Color>): ShaderGradient? {
            return when (list.size) {
                1 -> ShaderGradient(list[0])
                2 -> ShaderGradient(list[0], list[1])
                4 -> ShaderGradient(list[0], list[1], list[2], list[3])
                else -> null
            }
        }
    }

    constructor(
        argb: String,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = argb
        this.argbTopRight = argb
        this.argbBottomRight = argb
        this.argbBottomLeft = argb
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        argbTopLeftBottomRight: String,
        argbTopRightBottomLeft: String,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = argbTopLeftBottomRight
        this.argbTopRight = argbTopRightBottomLeft
        this.argbBottomRight = argbTopLeftBottomRight
        this.argbBottomLeft = argbTopRightBottomLeft
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        argbTopLeft: String,
        argbTopRight: String,
        argbBottomRight: String,
        argbBottomLeft: String,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = argbTopLeft
        this.argbTopRight = argbTopRight
        this.argbBottomLeft = argbBottomLeft
        this.argbBottomRight = argbBottomRight
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        color: Color,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = colorToArgb(color)
        this.argbTopRight = colorToArgb(color)
        this.argbBottomRight = colorToArgb(color)
        this.argbBottomLeft = colorToArgb(color)
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        colorTopLeftBottomRight: Color,
        colorTopRightBottomLeft: Color,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = colorToArgb(colorTopLeftBottomRight)
        this.argbTopRight = colorToArgb(colorTopRightBottomLeft)
        this.argbBottomRight = colorToArgb(colorTopLeftBottomRight)
        this.argbBottomLeft = colorToArgb(colorTopRightBottomLeft)
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    constructor(
        colorTopLeft: Color,
        colorTopRight: Color,
        colorBottomRight: Color,
        colorBottomLeft: Color,
        disabledSaturation: Float = 0.3f,
        enabledSaturation: Float = 1f
    ) : this() {
        this.argbTopLeft = colorToArgb(colorTopLeft)
        this.argbTopRight = colorToArgb(colorTopRight)
        this.argbBottomLeft = colorToArgb(colorBottomLeft)
        this.argbBottomRight = colorToArgb(colorBottomRight)
        this.disabledSaturation = disabledSaturation
        this.enabledSaturation = enabledSaturation
    }

    fun getColorTopLeft(isDisabled: Boolean): Int {
        return parseAndSaturateARGBToColor(
            argbTopLeft,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }

    fun getColorTopRight(isDisabled: Boolean): Int {
        return parseAndSaturateARGBToColor(
            argbTopRight,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }

    fun getColorBottomRight(isDisabled: Boolean): Int {
        return parseAndSaturateARGBToColor(
            argbBottomRight,
            if (isDisabled) disabledSaturation else enabledSaturation
        )
    }

    fun getColorBottomLeft(isDisabled: Boolean): Int {
        return parseAndSaturateARGBToColor(
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
