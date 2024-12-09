package dev.bittim.valolink.content.data.remote.dto.contract

import dev.bittim.valolink.content.data.local.entity.contract.ContentEntity

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