/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsAdsStatsConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsAdsStats
import kotlinx.serialization.json.Json

class ValoWeaponStatsAdsStatsConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toValoWeaponStatsAdsStats(value: String?): ValoWeaponStatsAdsStats? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromValoWeaponStatsAdsStats(value: ValoWeaponStatsAdsStats?): String? {
        return value?.let { json.encodeToString(it) }
    }
}