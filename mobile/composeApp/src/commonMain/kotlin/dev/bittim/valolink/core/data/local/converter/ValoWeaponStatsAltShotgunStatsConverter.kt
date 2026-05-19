/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsAltShotgunStatsConverter.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 11:48
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.data.local.embedded.ValoWeaponStatsAltShotgunStats
import kotlinx.serialization.json.Json

class ValoWeaponStatsAltShotgunStatsConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toValoWeaponStatsAltShotgunStats(value: String?): ValoWeaponStatsAltShotgunStats? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromValoWeaponStatsAltShotgunStats(value: ValoWeaponStatsAltShotgunStats?): String? {
        return value?.let { json.encodeToString(it) }
    }
}