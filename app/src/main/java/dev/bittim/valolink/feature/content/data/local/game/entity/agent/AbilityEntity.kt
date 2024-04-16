package dev.bittim.valolink.feature.content.data.local.game.entity.agent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.agent.Ability

@Entity(
    tableName = "AgentAbilities",
    foreignKeys = [
        ForeignKey(
            entity = AgentEntity::class,
            parentColumns = ["uuid"], childColumns = ["agentUuid"], onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AbilityEntity(
    @PrimaryKey val uuid: String, val agentUuid: String,
    override val version: String,
    val slot: String,
    val displayName: String,
    val description: String, val displayIcon: String?
) : GameEntity() {
    fun toType(): Ability {
        return Ability(
            agentUuid, slot, displayName, description, displayIcon
        )
    }
}