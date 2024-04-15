package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterLevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter

data class ChapterWithLevelsAndRewards(
    @Embedded val chapter: ChapterEntity,
    @Relation(
        entity = ChapterLevelEntity::class,
        parentColumn = "uuid",
        entityColumn = "chapterUuid"
    )
    val levels: List<LevelWithReward>,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "chapterUuid"
    )
    val freeRewards: List<RewardEntity>
) {
    fun toType() = Chapter(
        isEpilogue = chapter.isEpilogue,
        levels = levels.map { it.toType() },
        freeRewards = freeRewards.map { it.toType() }
    )
}