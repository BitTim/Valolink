package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

@Entity
data class ContractEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val displayIcon: String?,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val freeRewardScheduleUuid: String,
    @Embedded val content: Content,
    val assetPath: String
) : GameEntity<Contract>() {
    fun toType(chapters: List<Chapter>): Contract {
        return Contract(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon,
            shipIt = shipIt,
            useLevelVPCostOverride = useLevelVPCostOverride,
            levelVPCostOverride = levelVPCostOverride,
            freeRewardScheduleUuid = freeRewardScheduleUuid,
            content = content.toType(chapters)
        )
    }



    data class Content(
        val relationType: String?,
        val relationUuid: String?,
        val premiumRewardScheduleUuid: String?,
        val premiumVPCost: Int
    ) {
        fun toType(chapters: List<Chapter>) = Contract.Content(
            relationType = relationType,
            relationUuid = relationUuid,
            premiumRewardScheduleUuid = premiumRewardScheduleUuid,
            premiumVPCost = premiumVPCost,
            chapters = chapters
        )
    }
}