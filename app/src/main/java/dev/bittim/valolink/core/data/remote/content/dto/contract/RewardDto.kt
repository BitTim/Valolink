package dev.bittim.valolink.core.data.remote.content.dto.contract

import dev.bittim.valolink.core.data.local.content.entity.contract.RewardEntity

data class RewardDto(
	val type: String,
	val uuid: String,
	val amount: Int,
	val isHighlighted: Boolean,
) {
	fun toEntity(
		version: String,
		isFreeReward: Boolean,
		levelUuid: String,
	): RewardEntity {
		return RewardEntity(
			levelUuid + "_" + uuid,
			levelUuid,
			version,
			type,
			uuid,
			amount,
			isHighlighted,
			isFreeReward
		)
	}
}