/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponSkinChroma.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.weapon.skins

data class WeaponSkinChroma(
    val uuid: String,
    val chromaIndex: Int,
    val displayName: String,
    val displayIcon: String?,
    val fullRender: String,
    val swatch: String?,
    val streamedVideo: String?,
)
