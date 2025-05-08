/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContractWithContentWithChaptersWithLevelsAndRewards.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.contract.ContentEntity
import dev.bittim.valolink.content.data.local.entity.contract.ContractEntity
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.content.ContentRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation

data class ContractWithContentWithChaptersWithLevelsAndRewards(
    @Embedded val contract: ContractEntity,
    @Relation(
        entity = ContentEntity::class, parentColumn = "uuid", entityColumn = "contractUuid"
    ) val content: ContentWithChaptersWithLevelsAndRewards,
) : VersionedEntity {
    override val uuid: String
        get() = contract.uuid
    override val version: String
        get() = contract.version

    fun toType(
        relation: ContentRelation?,
        rewards: List<List<List<RewardRelation?>>>,
        levelNames: List<List<String>>,
    ): Contract {
        return contract.toType(
            content.toType(relation, rewards, levelNames)
        )
    }
}