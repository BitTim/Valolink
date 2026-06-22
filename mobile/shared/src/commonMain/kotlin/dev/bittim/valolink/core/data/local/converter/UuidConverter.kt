/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UuidConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

class UuidConverter {
    @TypeConverter
    fun toUuid(value: String?): Uuid? {
        return Uuid.parseOrNull(value ?: "")
    }

    @TypeConverter
    fun fromUuid(value: Uuid?): String? {
        return value?.toString()
    }
}