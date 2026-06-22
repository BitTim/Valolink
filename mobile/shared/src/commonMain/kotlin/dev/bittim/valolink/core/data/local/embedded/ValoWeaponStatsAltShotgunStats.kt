/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsAltShotgunStats.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.embedded

import kotlinx.serialization.Serializable

@Serializable
data class ValoWeaponStatsAltShotgunStats(
    val shotgunPelletCount: Int,
    val burstRate: Float
)
