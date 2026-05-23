/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsDamageRangesConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsDamageRange
import kotlinx.serialization.json.Json

class ValoWeaponStatsDamageRangesConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toValoWeaponStatsDamageRanges(value: String?): List<ValoWeaponStatsDamageRange>? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromValoWeaponStatsDamageRanges(values: List<ValoWeaponStatsDamageRange>?): String? {
        return values?.let { json.encodeToString(it) }
    }
}