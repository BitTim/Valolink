/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponSkinLevel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.weapon.skins

data class WeaponSkinLevel(
    val uuid: String,
    val levelIndex: Int,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
)
