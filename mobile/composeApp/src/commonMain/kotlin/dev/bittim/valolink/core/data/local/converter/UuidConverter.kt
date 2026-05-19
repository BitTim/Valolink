/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UuidConverter.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:34
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