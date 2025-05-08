/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankTableEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.local.entity.rank

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.rank.Rank
import dev.bittim.valolink.content.domain.model.rank.RankTable

@Entity(
    tableName = "RankTables",
    indices = [androidx.room.Index(
        value = ["uuid"],
        unique = true
    )]
)
data class RankTableEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val iteration: Int,
) : VersionedEntity {
    fun toType(ranks: List<Rank>): RankTable {
        return RankTable(
            uuid = uuid,
            ranks = ranks,
            iteration = iteration,
        )
    }
}
