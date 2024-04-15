package dev.bittim.valolink.feature.content.data.local.game.entity.agent

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.agent.Role

@Entity
data class RoleEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
    val assetPath: String
) : GameEntity() {
    fun toType(): Role {
        return Role(
            uuid = uuid,
            displayName = displayName,
            description = description,
            displayIcon = displayIcon
        )
    }
}
