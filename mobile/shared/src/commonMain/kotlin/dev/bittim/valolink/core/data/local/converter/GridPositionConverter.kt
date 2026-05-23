/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       GridPositionConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
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