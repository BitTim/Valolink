/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Weapon.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.weapon

import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponShopData
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkin
import dev.bittim.valolink.content.domain.model.weapon.stats.WeaponStats

data class Weapon(
    val uuid: String,
    val displayName: String,
    val category: String,
    val defaultSkinUuid: String,
    val displayIcon: String,
    val killStreamIcon: String,
    val weaponStats: WeaponStats?,
    val shopData: WeaponShopData?,
    val skins: List<WeaponSkin>,
)
