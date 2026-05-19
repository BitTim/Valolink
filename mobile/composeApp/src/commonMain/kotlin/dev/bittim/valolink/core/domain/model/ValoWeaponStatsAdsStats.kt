/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsAdsStats.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 02:24
 */

package dev.bittim.valolink.core.domain.model

data class ValoWeaponStatsAdsStats(
    val zoomMultiplier: Float,
    val fireRate: Float,
    val runSpeedMultiplier: Float,
    val burstCount: Int,
    val firstBulletAccuracy: Float
)
