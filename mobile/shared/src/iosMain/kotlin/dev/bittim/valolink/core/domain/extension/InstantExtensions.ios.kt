/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       InstantExtensions.ios.kt
 * Module:     Valolink.shared.iosMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 18:12
 */

package dev.bittim.valolink.core.domain.extension

import platform.Foundation.*
import kotlin.time.Instant

actual fun Instant.toLocalizedString(): String {
    val formatter = NSDateFormatter()
    formatter.dateStyle = NSDateFormatterShortStyle
    formatter.timeStyle = NSDateFormatterShortStyle
    formatter.timeZone = NSTimeZone.localTimeZone
    return formatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(this.epochSeconds.toDouble()))
}