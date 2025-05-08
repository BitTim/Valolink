/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.local.entity.rank

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.rank.Rank

@Entity(
    tableName = "Ranks",
    indices = [androidx.room.Index(
        value = ["uuid"],
        unique = true
    )],
    foreignKeys = [ForeignKey(
        entity = RankTableEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["rankTable"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class RankEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val rankTable: String,
    val tier: Int,
    val name: String,
    val divisionName: String,
    val color: String,
    val backgroundColor: String,
    val icon: String?,
    val triangleDownIcon: String?,
    val triangleUpIcon: String?,
) : VersionedEntity {
    fun toType(): Rank {
        return Rank(
            tier = tier,
            name = name,
            divisionName = divisionName,
            color = color,
            backgroundColor = backgroundColor,
            icon = icon,
            triangleDownIcon = triangleDownIcon,
            triangleUpIcon = triangleUpIcon
        )
    }
}
