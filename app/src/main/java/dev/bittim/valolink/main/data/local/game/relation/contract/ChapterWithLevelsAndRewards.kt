package dev.bittim.valolink.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Chapter
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation

data class ChapterWithLevelsAndRewards(
    @Embedded val chapter: ChapterEntity,
    @Relation(
        entity = LevelEntity::class,
        parentColumn = "uuid",
        entityColumn = "chapterUuid"
    ) val levels: List<LevelWithReward>,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "chapterUuid"
    ) val freeRewards: List<RewardEntity>,
) : VersionedEntity {
    override fun getApiVersion(): String {
        return chapter.version
    }

    fun toType(
        rewards: Pair<List<RewardRelation?>, List<RewardRelation?>>?,
        levelNames: List<String>,
        contractName: String,
    ): Chapter {
        return chapter.toType(
            levels.mapIndexed { index, level ->
                level.toType(
                    rewards?.first?.getOrNull(index),
                    levelNames[index],
                    contractName
                )
            },
            freeRewards.mapIndexed { index, freeReward ->
                freeReward.toType(rewards?.second?.getOrNull(index))
            }
        )
    }
}