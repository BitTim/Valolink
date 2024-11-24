package dev.bittim.valolink.core.data.remote.game.dto.weapon.shopData

import dev.bittim.valolink.core.data.local.game.entity.weapon.shopData.WeaponGridPositionEntity

data class WeaponGridPositionDto(
	val row: Int,
	val column: Int,
) {
	fun toEntity(
		version: String,
		weaponShopDataUuid: String,
	): WeaponGridPositionEntity {
		return WeaponGridPositionEntity(
			uuid = weaponShopDataUuid + "_" + row + "_" + column,
			version = version,
			weaponShopData = weaponShopDataUuid,
			row = row,
			column = column,
		)
	}
}