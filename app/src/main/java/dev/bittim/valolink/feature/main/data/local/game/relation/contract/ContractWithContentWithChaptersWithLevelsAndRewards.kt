package dev.bittim.valolink.feature.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.main.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.main.domain.model.game.contract.ContentRelation

data class ContractWithContentWithChaptersWithLevelsAndRewards(
    @Embedded val contract: ContractEntity,
    @Relation(
        entity = ContentEntity::class,
        parentColumn = "uuid",
        entityColumn = "contractUuid"
    ) val content: ContentWithChaptersWithLevelsAndRewards,
) {
    fun toType(
        relation: ContentRelation?,
    ): dev.bittim.valolink.feature.main.domain.model.game.contract.Contract {
        return contract.toType(
            content.toType(relation)
        )
    }
}