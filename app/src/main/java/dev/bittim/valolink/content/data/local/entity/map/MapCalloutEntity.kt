/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapCalloutEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.local.entity.map

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.map.MapCallout

@Entity(
    tableName = "MapCallouts", foreignKeys = [ForeignKey(
        entity = MapEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["map"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["map"], unique = false
    )]
)
data class MapCalloutEntity(
    @PrimaryKey override val uuid: String,
    val map: String,
    override val version: String,
    val regionName: String?,
    val superRegionName: String?,
    val x: Double,
    val y: Double,
) : VersionedEntity {
    fun toType(): MapCallout {
        return MapCallout(
            map,
            regionName,
            superRegionName,
            x,
            y
        )
    }
}
