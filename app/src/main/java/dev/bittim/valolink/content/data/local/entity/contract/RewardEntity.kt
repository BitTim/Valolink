/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RewardEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.contract.reward.Reward
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation

@Entity(
    tableName = "ContractRewards", foreignKeys = [ForeignKey(
        entity = LevelEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["levelUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["levelUuid"], unique = false
    )]
)
data class RewardEntity(
    @PrimaryKey override val uuid: String,
    val levelUuid: String,
    override val version: String,
    val rewardType: String,
    val rewardUuid: String,
    val amount: Int,
    val isHighlighted: Boolean,
    val isFreeReward: Boolean,
) : VersionedEntity {
    fun toType(relation: RewardRelation?): Reward {
        return Reward(
            rewardType, rewardUuid, amount, isHighlighted, isFreeReward, relation
        )
    }
}