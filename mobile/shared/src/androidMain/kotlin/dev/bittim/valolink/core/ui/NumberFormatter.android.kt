/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NumberFormatter.android.kt
 * Module:     Valolink.shared.androidMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   11.06.26, 23:53
 */

package dev.bittim.valolink.core.ui

import android.icu.text.MessageFormat

actual fun Int.toOrdinal(): String {
    return MessageFormat("{0,ordinal}").format(arrayOf(this))
}