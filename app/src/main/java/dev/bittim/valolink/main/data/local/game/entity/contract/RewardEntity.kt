package dev.bittim.valolink.main.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.contract.Reward

@Entity(
    tableName = "ContractRewards",
    foreignKeys = [ForeignKey(
        entity = LevelEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["levelUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = ChapterEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["chapterUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["levelUuid"],
        unique = false
    ), Index(
        value = ["chapterUuid"],
        unique = false
    )]
)
data class RewardEntity(
    @PrimaryKey val uuid: String,
    val levelUuid: String?,
    val chapterUuid: String?,
    override val version: String,
    val rewardType: String,
    val rewardUuid: String,
    val amount: Int,
    val isHighlighted: Boolean,
) : GameEntity() {
    fun toType(): Reward {
        return Reward(
            rewardType,
            rewardUuid,
            amount,
            isHighlighted
        )
    }
}