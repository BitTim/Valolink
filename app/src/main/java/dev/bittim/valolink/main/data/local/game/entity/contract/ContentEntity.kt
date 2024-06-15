package dev.bittim.valolink.main.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Chapter
import dev.bittim.valolink.main.domain.model.game.contract.content.Content
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation

@Entity(
    tableName = "ContractContents",
    foreignKeys = [ForeignKey(
        entity = ContractEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["contractUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["contractUuid"],
        unique = true
    )]
)
data class ContentEntity(
    @PrimaryKey val uuid: String,
    val contractUuid: String,
    override val version: String,
    val relationType: String?,
    val relationUuid: String?,
    val premiumRewardScheduleUuid: String?,
    val premiumVPCost: Int,
) : GameEntity() {
    fun toType(
        relation: ContentRelation?,
        chapters: List<Chapter>,
    ): Content {
        return Content(
            relation,
            premiumVPCost,
            chapters
        )
    }
}