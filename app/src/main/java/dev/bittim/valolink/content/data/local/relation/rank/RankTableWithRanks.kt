/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankTableWithRanks.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.local.relation.rank

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.rank.RankEntity
import dev.bittim.valolink.content.data.local.entity.rank.RankTableEntity
import dev.bittim.valolink.content.domain.model.rank.RankTable

data class RankTableWithRanks(
    @Embedded val rankTable: RankTableEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "rankTable"
    ) val ranks: List<RankEntity>
) : VersionedEntity {
    override val uuid: String
        get() = rankTable.uuid
    override val version: String
        get() = rankTable.version

    fun toType(): RankTable {
        return rankTable.toType(ranks.map { it.toType() })
    }
}
