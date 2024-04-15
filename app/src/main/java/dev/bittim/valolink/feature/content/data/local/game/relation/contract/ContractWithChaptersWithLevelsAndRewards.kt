package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class ContractWithChaptersWithLevelsAndRewards(
    @Embedded val contract: ContractEntity,
    @Relation(
        entity = ContractContentEntity::class,
        parentColumn = "uuid",
        entityColumn = "contractUuid"
    )
    val content: ContentWithChaptersWithLevelsAndRewards
) {
    fun toType() = Contract(
        uuid = contract.uuid,
        displayName = contract.displayName,
        displayIcon = contract.displayIcon,
        shipIt = contract.shipIt,
        useLevelVPCostOverride = contract.useLevelVPCostOverride,
        levelVPCostOverride = contract.levelVPCostOverride,
        freeRewardScheduleUuid = contract.freeRewardScheduleUuid,
        content = content.toType()
    )
}