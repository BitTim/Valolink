package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Reward

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ContractEntity::class,
            parentColumns = [ "uuid" ],
            childColumns = [ "contractUuid" ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val contractUuid: String,
    override val version: String,
    val isEpilogue: Boolean
) : GameEntity<Chapter>() {
    fun toType(levels: List<ChapterLevel>, freeRewards: List<Reward>) = Chapter (
        levels = levels,
        freeRewards = freeRewards,
        isEpilogue = isEpilogue
    )
}