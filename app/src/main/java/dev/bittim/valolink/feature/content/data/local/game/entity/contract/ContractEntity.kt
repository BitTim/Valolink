package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

@Entity(tableName = "contracts")
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
    fun toType(content: Contract.Content): Contract {
        return Contract(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon,
            shipIt = shipIt,
            useLevelVPCostOverride = useLevelVPCostOverride,
            levelVPCostOverride = levelVPCostOverride,
            freeRewardScheduleUuid = freeRewardScheduleUuid,
            content = content
        )
    }
}