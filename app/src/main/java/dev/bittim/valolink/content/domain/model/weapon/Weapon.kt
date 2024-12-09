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
