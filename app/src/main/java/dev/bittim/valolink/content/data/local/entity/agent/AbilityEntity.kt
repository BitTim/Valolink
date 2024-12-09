package dev.bittim.valolink.content.data.local.entity.agent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.agent.Ability

@Entity(
    tableName = "AgentAbilities", foreignKeys = [ForeignKey(
        entity = AgentEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["agentUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["id"], unique = true
    ), Index(
        value = ["agentUuid"], unique = false
    )]
)
data class AbilityEntity(
    @PrimaryKey val id: String,
    val agentUuid: String,
    override val version: String,
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String?,
) : VersionedEntity {
    fun toType(): Ability {
        return Ability(
            agentUuid, slot, displayName, description, displayIcon
        )
    }
}