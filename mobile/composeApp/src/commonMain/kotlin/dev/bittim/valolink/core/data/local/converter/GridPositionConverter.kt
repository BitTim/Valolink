/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       GridPositionConverter.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 12:07
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.data.local.embedded.GridPosition
import kotlinx.serialization.json.Json

class GridPositionConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toGridPosition(value: String?): GridPosition? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromGridPosition(value: GridPosition?): String? {
        return value?.let { json.encodeToString(it) }
    }
}