package dev.bittim.valolink.content.data.remote.dto.weapon

import dev.bittim.valolink.content.data.local.entity.weapon.WeaponEntity
import dev.bittim.valolink.content.data.remote.dto.weapon.shopData.WeaponShopDataDto
import dev.bittim.valolink.content.data.remote.dto.weapon.skin.WeaponSkinDto
import dev.bittim.valolink.content.data.remote.dto.weapon.stats.WeaponStatsDto

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