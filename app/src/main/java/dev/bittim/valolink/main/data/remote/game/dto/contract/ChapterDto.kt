package dev.bittim.valolink.main.data.remote.game.dto.contract

import dev.bittim.valolink.main.data.local.game.entity.contract.ChapterEntity

data class ChapterDto(
    val isEpilogue: Boolean,
    val levels: List<ChapterLevelDto>,
    val freeRewards: List<RewardDto>?,
) {
    fun toEntity(
        index: Int,
        version: String,
        contentUuid: String,
    ): ChapterEntity {
        return ChapterEntity(
            contentUuid + "_" + index,
            contentUuid,
            version,
            isEpilogue
        )
    }
}