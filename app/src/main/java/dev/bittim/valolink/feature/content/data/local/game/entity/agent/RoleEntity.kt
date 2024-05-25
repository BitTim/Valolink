package dev.bittim.valolink.feature.content.data.local.game.entity.agent

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity

@Entity(
    tableName = "AgentRoles",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class RoleEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
    val assetPath: String,
) : GameEntity() {
    fun toType(): dev.bittim.valolink.feature.content.domain.model.game.agent.Role {
        return dev.bittim.valolink.feature.content.domain.model.game.agent.Role(
            uuid,
            displayName,
            description,
            displayIcon
        )
    }
}
