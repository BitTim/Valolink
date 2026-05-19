/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsAirBurstStatsConverter.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 11:47
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