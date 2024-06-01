package dev.bittim.valolink.main.data.local.user.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.domain.model.UserData
import java.time.OffsetDateTime

@Entity(
    tableName = "UserData",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class UserDataEntity(
    @PrimaryKey val uuid: String,
    override val isSynced: Boolean,
    override val updatedAt: String,
    val username: String,
    val agents: List<String>,
) : SyncedEntity() {
    fun toType(): UserData {
        return UserData(
            uuid,
            username,
            agents
        )
    }

    companion object {
        fun fromType(userData: UserData): UserDataEntity {
            return UserDataEntity(
                userData.uuid,
                false,
                OffsetDateTime.now().toString(),
                userData.username,
                userData.agents
            )
        }
    }
}
