package dev.bittim.valolink.feature.content.data.local.game.entity.agent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.agent.Ability

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AgentEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["agent"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AbilityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    override val version: String,
    val agent: String,
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String
) : GameEntity() {
    fun toType(): Ability {
        return Ability(
            agent = agent,
            slot = slot,
            displayName = displayName,
            description = description,
            displayIcon = displayIcon
        )
    }
}