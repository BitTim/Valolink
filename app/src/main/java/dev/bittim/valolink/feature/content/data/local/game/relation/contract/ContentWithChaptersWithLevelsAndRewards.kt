package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractContentEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class ContentWithChaptersWithLevelsAndRewards(
    @Embedded val content: ContractContentEntity,
    @Relation(
        entity = ChapterEntity::class,
        parentColumn = "uuid",
        entityColumn = "contentUuid"
    )
    val chapters: List<ChapterWithLevelsAndRewards>
) {
    fun toType() = Contract.Content(
        relationType = content.relationType,
        relationUuid = content.relationUuid,
        premiumRewardScheduleUuid = content.premiumRewardScheduleUuid,
        premiumVPCost = content.premiumVPCost,
        chapters = chapters.map { it.toType() }
    )
}
