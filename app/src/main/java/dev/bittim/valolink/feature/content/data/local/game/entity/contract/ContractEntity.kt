package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Content
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

@Entity(
    tableName = "Contracts", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
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
    fun toType(content: Content): Contract {
        return Contract(
            uuid, displayName, useLevelVPCostOverride, levelVPCostOverride, content
        )
    }
}