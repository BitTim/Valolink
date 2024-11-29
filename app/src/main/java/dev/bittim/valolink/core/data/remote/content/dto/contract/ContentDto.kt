package dev.bittim.valolink.core.data.remote.content.dto.contract

import dev.bittim.valolink.core.data.local.content.entity.contract.ContentEntity

data class ContentDto(
	val relationType: String?,
	val relationUuid: String?,
	val chapters: List<ChapterDto>,
	val premiumRewardScheduleUuid: String?,
	val premiumVPCost: Int,
) {
	fun toEntity(
		uuid: String,
		version: String,
		contractUuid: String,
	): ContentEntity {
		return ContentEntity(
			uuid,
			contractUuid,
			version,
			relationType,
			relationUuid,
			premiumRewardScheduleUuid,
			premiumVPCost
		)
	}
}