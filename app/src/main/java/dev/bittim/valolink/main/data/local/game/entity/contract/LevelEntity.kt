package dev.bittim.valolink.main.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward

@Entity(
    tableName = "ContractChapterLevels",
    foreignKeys = [ForeignKey(
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
        value = ["chapterUuid"],
        unique = false
    )]
)
data class LevelEntity(
    @PrimaryKey val uuid: String,
    val chapterUuid: String,
    override val version: String,
    val dependency: String?,
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
) : GameEntity() {
    fun toType(name: String, rewards: List<Reward>): Level {
        return Level(
            uuid,
            dependency,
            name,
            xp,
            vpCost,
            isPurchasableWithVP,
            doughCost,
            isPurchasableWithDough,
            rewards
        )
    }
}