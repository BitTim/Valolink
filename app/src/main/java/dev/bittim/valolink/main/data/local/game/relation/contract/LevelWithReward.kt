package dev.bittim.valolink.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation

data class LevelWithReward(
    @Embedded val level: LevelEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "levelUuid"
    ) val reward: RewardEntity,
) : VersionedEntity {
    override fun getApiVersion(): String {
        return level.version
    }

    fun toType(
        relation: RewardRelation?,
        levelName: String,
        contractName: String,
    ): Level {
        return level.toType(
            levelName,
            contractName,
            reward.toType(relation),
        )
    }
}
