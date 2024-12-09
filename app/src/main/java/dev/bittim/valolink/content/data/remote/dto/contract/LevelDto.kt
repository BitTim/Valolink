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