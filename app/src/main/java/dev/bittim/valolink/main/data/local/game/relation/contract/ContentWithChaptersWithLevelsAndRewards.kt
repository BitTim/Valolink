package dev.bittim.valolink.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.main.domain.model.game.contract.content.Content
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation

data class ContentWithChaptersWithLevelsAndRewards(
    @Embedded val content: ContentEntity,
    @Relation(
        entity = ChapterEntity::class,
        parentColumn = "uuid",
        entityColumn = "contentUuid"
    ) val chapters: List<ChapterWithLevelsAndRewards>,
) : VersionedEntity {
    override fun getApiVersion(): String {
        return content.version
    }

    fun toType(
        relation: ContentRelation?,
        rewards: List<Pair<List<RewardRelation?>, List<RewardRelation?>>>,
        levelNames: List<List<String>>,
    ): Content {
        return content.toType(
            relation,
            chapters.mapIndexed { index, rawChapter ->
                rawChapter.toType(rewards.getOrNull(index), levelNames[index])
            }
        )
    }
}