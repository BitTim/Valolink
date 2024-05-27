package dev.bittim.valolink.feature.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.main.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.feature.main.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.feature.main.domain.model.game.contract.ChapterLevel

data class LevelWithReward(
    @Embedded val level: LevelEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "levelUuid"
    )
    val reward: RewardEntity
) {
    fun toType(): ChapterLevel {
        return level.toType(
            reward.toType()
        )
    }
}