/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LevelWithRewards.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.contract.LevelEntity
import dev.bittim.valolink.content.data.local.entity.contract.RewardEntity
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation

data class LevelWithRewards(
    @Embedded val level: LevelEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "levelUuid"
    ) val rewards: List<RewardEntity>,
) : VersionedEntity {
    override val uuid: String
        get() = level.uuid
    override val version: String
        get() = level.version

    fun toType(
        relations: List<RewardRelation?>,
        levelName: String,
    ): Level {
        return level.toType(
            levelName,
            this.rewards.zip(relations) { reward, relation -> reward.toType(relation) },
        )
    }
}
