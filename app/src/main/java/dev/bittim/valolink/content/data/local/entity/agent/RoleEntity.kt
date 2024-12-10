package dev.bittim.valolink.content.data.local.entity.agent

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.agent.Role

@Entity(
    tableName = "AgentRoles", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class RoleEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
    val assetPath: String,
) : VersionedEntity {
    fun toType(): Role {
        return Role(
            uuid, displayName, description, displayIcon
        )
    }
}