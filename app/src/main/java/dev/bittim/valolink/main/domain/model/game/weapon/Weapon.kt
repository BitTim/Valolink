package dev.bittim.valolink.main.domain.model.game.weapon

import dev.bittim.valolink.main.domain.model.game.weapon.shopData.WeaponShopData
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponStats

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
