package dev.bittim.valolink.main.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Chapter
import dev.bittim.valolink.main.domain.model.game.contract.chapter.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward

@Entity(
    tableName = "ContractChapters",
    foreignKeys = [ForeignKey(
        entity = ContentEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["contentUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(
            value = ["uuid"],
            unique = true
        ),
        Index(
            value = ["contentUuid"],
            unique = false
        ),
    ]
)
data class ChapterEntity(
    @PrimaryKey val uuid: String,
    val contentUuid: String,
    override val version: String,
    val isEpilogue: Boolean,
) : GameEntity() {
    fun toType(
        levels: List<ChapterLevel>,
        freeRewards: List<Reward>?,
    ): Chapter {
        return Chapter(
            levels,
            freeRewards,
            isEpilogue
        )
    }
}