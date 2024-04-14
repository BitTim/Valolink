package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Reward

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChapterLevelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val chapterId: Int,
    override val version: String,
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
) : GameEntity<ChapterLevel>() {
    fun toType(reward: Reward) = ChapterLevel(
        xp = xp,
        vpCost = vpCost,
        isPurchasableWithVP = isPurchasableWithVP,
        doughCost = doughCost,
        isPurchasableWithDough = isPurchasableWithDough,
        reward = reward
    )
}