package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

@Entity(
    tableName = "contractContents",
    foreignKeys = [
        ForeignKey(
            entity = ContractEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["contractUuid"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ContractContentEntity(
    @PrimaryKey
    val uuid: String,
    val contractUuid: String,
    override val version: String,
    val relationType: String?,
    val relationUuid: String?,
    val premiumRewardScheduleUuid: String?,
    val premiumVPCost: Int
) : GameEntity() {
    fun toType(chapters: List<Chapter>) = Contract.Content(
        relationType = relationType,
        relationUuid = relationUuid,
        premiumRewardScheduleUuid = premiumRewardScheduleUuid,
        premiumVPCost = premiumVPCost,
        chapters = chapters
    )
}