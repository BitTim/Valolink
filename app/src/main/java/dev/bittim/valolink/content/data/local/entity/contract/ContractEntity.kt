package dev.bittim.valolink.content.data.local.entity.contract

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.content.Content

@Entity(
    tableName = "Contracts", indices = [Index(
        value = ["uuid"], unique = true
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
) : VersionedEntity {
    fun toType(content: Content): Contract {
        return Contract(
            uuid, displayName, useLevelVPCostOverride, levelVPCostOverride, content
        )
    }
}