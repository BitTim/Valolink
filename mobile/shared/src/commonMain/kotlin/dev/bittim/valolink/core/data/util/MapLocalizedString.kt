/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapLocalizedString.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 13:44
 */

package dev.bittim.valolink.core.data.util

const val fallbackLocale = "en-US"

fun Map<String, String>.localized(locale: String?): String {
    return get(locale)
        ?: get(fallbackLocale)
        ?: values.firstOrNull()
        ?: ""
}