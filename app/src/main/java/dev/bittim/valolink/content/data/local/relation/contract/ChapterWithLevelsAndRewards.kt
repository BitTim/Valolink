/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ChapterWithLevelsAndRewards.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.contract.ChapterEntity
import dev.bittim.valolink.content.data.local.entity.contract.LevelEntity
import dev.bittim.valolink.content.domain.model.contract.chapter.Chapter
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation

data class ChapterWithLevelsAndRewards(
    @Embedded val chapter: ChapterEntity,
    @Relation(
        entity = LevelEntity::class, parentColumn = "uuid", entityColumn = "chapterUuid"
    ) val levels: List<LevelWithRewards>,
) : VersionedEntity {
    override val version: String
        get() = chapter.version

    fun toType(
        rewards: List<List<RewardRelation?>>,
        levelNames: List<String>,
    ): Chapter {
        return chapter.toType(levels.mapIndexed { index, level ->
            level.toType(
                rewards.getOrNull(index) ?: emptyList(),
                levelNames[index],
            )
        })
    }
}