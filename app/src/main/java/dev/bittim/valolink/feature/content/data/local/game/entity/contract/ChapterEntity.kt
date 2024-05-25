package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.game.contract.Reward

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
    ): dev.bittim.valolink.feature.content.domain.model.game.contract.Chapter {
        return dev.bittim.valolink.feature.content.domain.model.game.contract.Chapter(
            levels,
            freeRewards,
            isEpilogue
        )
    }
}