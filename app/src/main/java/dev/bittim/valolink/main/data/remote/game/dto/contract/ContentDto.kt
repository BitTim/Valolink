package dev.bittim.valolink.main.data.remote.game.dto.contract

import dev.bittim.valolink.main.data.local.game.entity.contract.ContentEntity

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