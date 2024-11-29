package dev.bittim.valolink.core.data.remote.content.dto.contract

import dev.bittim.valolink.core.data.local.content.entity.contract.ChapterEntity

data class ChapterDto(
	val isEpilogue: Boolean,
	val levels: List<LevelDto>,
	val freeRewards: List<RewardDto>?,
) {
	fun toEntity(
		index: Int,
		version: String,
		contentUuid: String,
	): ChapterEntity {
		return ChapterEntity(
			contentUuid + "_" + index, contentUuid, version, isEpilogue
		)
	}
}