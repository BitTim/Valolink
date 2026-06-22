/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NumberFormatter.ios.kt
 * Module:     Valolink.shared.iosMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   11.06.26, 23:59
 */

package dev.bittim.valolink.core.ui

import platform.Foundation.*

actual fun Int.toOrdinal(): String {
    return NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterOrdinalStyle
        locale = NSLocale.currentLocale
    }.stringFromNumber(NSNumber(this)) ?: toString()
}