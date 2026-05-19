/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponStatsDamageRange.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 02:39
 */

package dev.bittim.valolink.core.domain.model

data class ValoWeaponStatsDamageRange(
    val rangeStartMeters: Int,
    val rangeEndMeters: Int,
    val headDamage: Float,
    val bodyDamage: Float,
    val legDamage: Float
)
