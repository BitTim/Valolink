package dev.bittim.valolink.core.data.remote.content.dto.weapon.shopData

import dev.bittim.valolink.core.data.local.content.entity.weapon.shopData.WeaponShopDataEntity

data class WeaponShopDataDto(
	val cost: Int,
	val category: String,
	val shopOrderPriority: Int,
	val categoryText: String,
	val gridPosition: WeaponGridPositionDto?,
	val canBeTrashed: Boolean,
	val image: String?,
	val newImage: String,
	val newImage2: String?,
	val assetPath: String,
) {
	fun toEntity(
		version: String,
		weaponUuid: String,
	): WeaponShopDataEntity {
		return WeaponShopDataEntity(
			uuid = weaponUuid,
			version = version,
			weapon = weaponUuid,
			cost = cost,
			category = category,
			shopOrderPriority = shopOrderPriority,
			categoryText = categoryText,
			canBeTrashed = canBeTrashed,
			image = image,
			newImage = newImage,
			newImage2 = newImage2,
		)
	}
}