/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       StringListConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class StringListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.let { json.decodeFromString(ListSerializer(String.serializer()), it) }
    }

    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.let { json.encodeToString(ListSerializer(String.serializer()), it) }
    }
}