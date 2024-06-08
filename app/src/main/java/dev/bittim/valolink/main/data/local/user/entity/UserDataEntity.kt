package dev.bittim.valolink.main.data.local.user.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.UserData
import java.time.OffsetDateTime

@Entity(
    tableName = "Users",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class UserDataEntity(
    @PrimaryKey val uuid: String,
    override val isSynced: Boolean,
    override val updatedAt: String,
    val isPrivate: Boolean,
    val username: String,
    val agents: List<String>,
) : SyncedEntity() {
    fun toType(): UserData {
        return UserData(
            uuid,
            isPrivate,
            username,
            agents,
            listOf()
        )
    }

    companion object {
        fun fromType(userData: UserData): UserDataEntity {
            return UserDataEntity(
                userData.uuid,
                false,
                OffsetDateTime.now().toString(),
                userData.isPrivate,
                userData.username,
                userData.agents
            )
        }
    }
}
