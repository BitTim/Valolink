/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       InstantExtensions.android.kt
 * Module:     Valolink.shared.androidMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:04
 */

package dev.bittim.valolink.core.domain.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@RequiresApi(Build.VERSION_CODES.O)
actual fun Instant.toLocalizedString(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    return toJavaInstant().atZone(ZoneId.systemDefault()).format(formatter)
}