/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       GenerateProfilePictureUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   11.04.25, 00:25
 */

package dev.bittim.valolink.core.domain.usecase.profile

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.createBitmap
import java.security.MessageDigest
import javax.inject.Inject
import kotlin.random.Random

class GenerateProfilePictureUseCase @Inject constructor() {
    private val width = 500
    private val height = 500
    private val fontSize = 300f

    operator fun invoke(
        username: String
    ): Bitmap {
        // Set up randomizer
        val md = MessageDigest.getInstance("MD5")
        val rawHash = md.digest(username.toByteArray())
        var hash: Long = 0
        for (i in 0 until 8) {
            hash = hash or (rawHash[i].toLong() and 0xFF shl (8 * (7 - i)))
        }

        val rand = Random(seed = hash)

        // Generate color
        var hsvPrimary = FloatArray(3)
        hsvPrimary[0] = rand.nextFloat() * 360          // 0 - 360
        hsvPrimary[1] = rand.nextFloat() * 0.7f + 0.1f     // 0.1 - 0.8
        hsvPrimary[2] = rand.nextFloat() * 0.5f + 0.25f     // 0.25 - 0.75
        val colPrimary = Color.HSVToColor(hsvPrimary)

        // Generate image
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        paint.color = Color.WHITE
        paint.textSize = fontSize

        canvas.drawColor(colPrimary)

        val capitals = username.filter { c -> c.isUpperCase() || c.isDigit() }
        var initials = capitals.take(2)
        if (initials.isEmpty()) {
            initials = username.take(2)
        }

        val textBounds = Rect()
        paint.getTextBounds(initials, 0, initials.length, textBounds)

        val centerX = width / 2f - textBounds.width() / 2f
        canvas.drawText(initials, centerX, height / 2f + textBounds.height() / 2f, paint)

        return bitmap
    }
}
