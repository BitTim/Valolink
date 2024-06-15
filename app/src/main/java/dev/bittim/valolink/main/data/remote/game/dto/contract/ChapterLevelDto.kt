package dev.bittim.valolink.main.data.remote.game.dto.contract

import dev.bittim.valolink.main.data.local.game.entity.contract.LevelEntity

data class ChapterLevelDto(
    val reward: RewardDto,
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
) {
    fun toEntity(
        index: Int,
        version: String,
        chapterUuid: String,
    ): LevelEntity {
        return LevelEntity(
            chapterUuid + "_" + index,
            chapterUuid,
            version,
            xp,
            vpCost,
            isPurchasableWithVP,
            doughCost,
            isPurchasableWithDough
        )
    }
}