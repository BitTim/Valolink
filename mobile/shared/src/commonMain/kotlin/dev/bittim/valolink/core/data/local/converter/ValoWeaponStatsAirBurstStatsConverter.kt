/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsAirBurstStatsConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsAirBurstStats
import kotlinx.serialization.json.Json

class ValoWeaponStatsAirBurstStatsConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toValoWeaponStatsAirBurstStats(value: String?): ValoWeaponStatsAirBurstStats? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromValoWeaponStatsAirBurstStats(value: ValoWeaponStatsAirBurstStats?): String? {
        return value?.let { json.encodeToString(it) }
    }
}