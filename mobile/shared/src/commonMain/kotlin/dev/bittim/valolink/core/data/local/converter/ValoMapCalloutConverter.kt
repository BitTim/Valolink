/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapCalloutConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.data.local.embedded.ValoMapCallout
import kotlinx.serialization.json.Json

class ValoMapCalloutConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toValoMapCallouts(value: String?): List<ValoMapCallout>? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromValoMapCallouts(values: List<ValoMapCallout>?): String? {
        return values?.let { json.encodeToString(it) }
    }
}