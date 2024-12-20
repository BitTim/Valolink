package dev.bittim.valolink.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation

data class LevelWithRewards(
    @Embedded val level: LevelEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "levelUuid"
    ) val rewards: List<RewardEntity>,
) : VersionedEntity {
    override fun getApiVersion(): String {
        return level.version
    }

    fun toType(
        relations: List<RewardRelation?>,
        levelName: String,
    ): Level {
        return level.toType(
            levelName,
            this.rewards.zip(relations) { reward, relation -> reward.toType(relation) },
        )
    }
}
