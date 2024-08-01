package dev.bittim.valolink.main.data.local.user.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.user.UserAgent
import java.time.OffsetDateTime

@Entity(
    tableName = "UserAgents",
    foreignKeys = [
        ForeignKey(
            entity = UserDataEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["user"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["user", "agent"],
        unique = true
    )]
)
data class UserAgentEntity(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val user: String,
    val agent: String,
) : SyncedEntity {
    override fun getIdentifier(): String {
        return agent
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        return this.copy(isSynced = true)
    }

    fun toType(): UserAgent {
        return UserAgent(
            uuid,
            user,
            agent
        )
    }

    companion object {
        fun fromType(userAgent: UserAgent, isSynced: Boolean, toDelete: Boolean): UserAgentEntity {
            return UserAgentEntity(
                userAgent.uuid,
                isSynced,
                toDelete,
                OffsetDateTime.now().toString(),
                userAgent.user,
                userAgent.agent
            )
        }
    }
}
