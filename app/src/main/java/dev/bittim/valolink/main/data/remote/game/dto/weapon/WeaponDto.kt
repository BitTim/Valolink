package dev.bittim.valolink.main.data.remote.game.dto.weapon

import dev.bittim.valolink.main.data.local.game.entity.weapon.WeaponEntity
import dev.bittim.valolink.main.data.remote.game.dto.weapon.shopData.WeaponShopDataDto
import dev.bittim.valolink.main.data.remote.game.dto.weapon.skin.WeaponSkinDto
import dev.bittim.valolink.main.data.remote.game.dto.weapon.stats.WeaponStatsDto

data class WeaponDto(
    val uuid: String,
    val displayName: String,
    val category: String,
    val defaultSkinUuid: String,
    val displayIcon: String,
    val killStreamIcon: String,
    val assetPath: String,
    val weaponStats: WeaponStatsDto?,
    val shopData: WeaponShopDataDto?,
    val skins: List<WeaponSkinDto>,
) {
    fun toEntity(
        version: String,
    ): WeaponEntity {
        return WeaponEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            category = category,
            defaultSkinUuid = defaultSkinUuid,
            displayIcon = displayIcon,
            killStreamIcon = killStreamIcon,
        )
    }
}