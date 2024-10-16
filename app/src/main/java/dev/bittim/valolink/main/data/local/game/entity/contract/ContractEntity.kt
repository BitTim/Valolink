package dev.bittim.valolink.main.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.content.Content

@Entity(
    tableName = "Contracts",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class ContractEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val displayIcon: String?,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val freeRewardScheduleUuid: String,
    val assetPath: String,
) : GameEntity() {
    fun toType(content: Content): Contract {
        return Contract(
            uuid,
            displayName,
            useLevelVPCostOverride,
            levelVPCostOverride,
            content
        )
    }
}