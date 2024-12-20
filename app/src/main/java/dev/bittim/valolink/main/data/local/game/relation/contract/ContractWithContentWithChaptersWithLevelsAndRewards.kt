package dev.bittim.valolink.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.main.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation

data class ContractWithContentWithChaptersWithLevelsAndRewards(
    @Embedded val contract: ContractEntity,
    @Relation(
        entity = ContentEntity::class,
        parentColumn = "uuid",
        entityColumn = "contractUuid"
    ) val content: ContentWithChaptersWithLevelsAndRewards,
) : VersionedEntity {
    override fun getApiVersion(): String {
        return contract.version
    }

    fun toType(
        relation: ContentRelation?,
        rewards: List<List<List<RewardRelation?>>>,
        levelNames: List<List<String>>,
    ): Contract {
        return contract.toType(
            content.toType(relation, rewards, levelNames)
        )
    }
}