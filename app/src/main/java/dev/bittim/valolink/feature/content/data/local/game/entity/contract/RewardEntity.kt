package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Reward

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["levelId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RewardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val levelId: Int?,
    val chapterId: Int?,
    override val version: String,
    val type: String,
    val uuid: String,
    val amount: Int,
    val isHighlighted: Boolean
) : GameEntity<Reward>() {
    override fun toType(): Reward {
        return Reward(
            type = type,
            uuid = uuid,
            amount = amount,
            isHighlighted = isHighlighted
        )
    }
}