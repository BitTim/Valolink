/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponDamageRange.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.weapon.stats

data class WeaponDamageRange(
    val rangeStartMeters: Double,
    val rangeEndMeters: Double,
    val headDamage: Double,
    val bodyDamage: Double,
    val legDamage: Double,
)
