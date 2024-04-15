package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Reward

@Entity(
    tableName = "contractChapterLevels",
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["chapterUuid"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChapterLevelEntity(
    @PrimaryKey
    val uuid: String,
    val chapterUuid: String,
    override val version: String,
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
) : GameEntity() {
    fun toType(reward: Reward) = ChapterLevel(
        xp = xp,
        vpCost = vpCost,
        isPurchasableWithVP = isPurchasableWithVP,
        doughCost = doughCost,
        isPurchasableWithDough = isPurchasableWithDough,
        reward = reward
    )
}