/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LevelDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.contract

import dev.bittim.valolink.content.data.local.entity.contract.LevelEntity

data class LevelDto(
	val reward: RewardDto,
	val xp: Int,
	val vpCost: Int,
	val isPurchasableWithVP: Boolean,
	val doughCost: Int,
	val isPurchasableWithDough: Boolean,
) {
	fun toEntity(
		index: Int,
		dependency: String?,
		version: String,
		chapterUuid: String,
	): LevelEntity {
		return LevelEntity(
			chapterUuid + "_" + index,
			chapterUuid,
			version,
			dependency,
			xp,
			vpCost,
			isPurchasableWithVP,
			doughCost,
			isPurchasableWithDough
		)
	}
}