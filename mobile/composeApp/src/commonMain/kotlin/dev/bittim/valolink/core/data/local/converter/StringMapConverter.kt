/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       StringMapConverter.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:33
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class StringMapConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toMap(value: String?): Map<String, String>? {
        return value?.let { json.decodeFromString(MapSerializer(String.serializer(), String.serializer()), it) }
    }

    @TypeConverter
    fun fromMap(value: Map<String, String>?): String? {
        return value?.let { json.encodeToString(MapSerializer(String.serializer(), String.serializer()), it) }
    }
}