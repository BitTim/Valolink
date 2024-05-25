package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.game.contract.Content
import dev.bittim.valolink.feature.content.domain.model.game.contract.ContentRelation

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
        chapters: List<dev.bittim.valolink.feature.content.domain.model.game.contract.Chapter>,
    ): Content {
        return Content(
            relation,
            premiumVPCost,
            chapters
        )
    }
}