package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.ContractRelation

data class ContractWithChaptersWithLevelsAndRewards(
    @Embedded val contract: ContractEntity,
    @Relation(
        entity = ContentEntity::class,
        parentColumn = "uuid",
        entityColumn = "contractUuid"
    )
    val content: ContentWithChaptersWithLevelsAndRewards
) {
    fun toType(relation: ContractRelation?): Contract {
        return contract.toType(
            content.toType(), relation
        )
    }
}