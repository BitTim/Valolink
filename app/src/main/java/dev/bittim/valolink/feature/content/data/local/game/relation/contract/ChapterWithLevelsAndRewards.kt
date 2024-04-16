package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter

data class ChapterWithLevelsAndRewards(
    @Embedded val chapter: ChapterEntity,
    @Relation(
        entity = LevelEntity::class,
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
    fun toType(): Chapter {
        return chapter.toType(levels.map { it.toType() }, freeRewards.map { it.toType() })
    }
}