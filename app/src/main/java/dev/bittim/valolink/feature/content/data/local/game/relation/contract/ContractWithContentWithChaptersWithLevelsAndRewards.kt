package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.domain.model.contract.ContentRelation
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import java.time.ZonedDateTime

data class ContractWithContentWithChaptersWithLevelsAndRewards(
    @Embedded val contract: ContractEntity,
    @Relation(
        entity = ContentEntity::class,
        parentColumn = "uuid",
        entityColumn = "contractUuid"
    )
    val content: ContentWithChaptersWithLevelsAndRewards
) {
    fun toType(
        relation: ContentRelation?,
        startTime: ZonedDateTime?,
        endTime: ZonedDateTime?
    ): Contract {
        return contract.toType(
            content.toType(relation), startTime, endTime
        )
    }
}