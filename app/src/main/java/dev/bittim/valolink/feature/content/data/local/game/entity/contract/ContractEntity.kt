package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Content
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.ContractRelation

@Entity(tableName = "Contracts")
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
    val assetPath: String
) : GameEntity() {
    fun toType(content: Content, relation: ContractRelation?): Contract {
        return Contract(
            uuid,
            displayName,
            displayIcon,
            shipIt,
            useLevelVPCostOverride,
            levelVPCostOverride,
            freeRewardScheduleUuid,
            content,
            relation
        )
    }
}