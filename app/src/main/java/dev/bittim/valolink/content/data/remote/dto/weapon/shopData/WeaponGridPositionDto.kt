/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponGridPositionDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.remote.dto.weapon.shopData

import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponGridPositionEntity

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