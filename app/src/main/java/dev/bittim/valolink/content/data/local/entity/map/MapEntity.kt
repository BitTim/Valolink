/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.local.entity.map

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.map.GameMap
import dev.bittim.valolink.content.domain.model.map.MapCallout
import dev.bittim.valolink.content.domain.model.map.MapType

@Entity(
    tableName = "Maps",
    indices = [Index(
        value = ["uuid"], unique = true
    )]
)
data class MapEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val displayName: String,
    val narrativeDescription: String?,
    val tacticalDescription: String?,
    val coordinates: String?,
    val displayIcon: String?,
    val listViewIcon: String?,
    val listViewIconTall: String?,
    val splash: String?,
    val stylizedBackgroundImage: String?,
    val premierBackgroundImage: String?,
    val type: MapType,
    val xMultiplier: Double,
    val yMultiplier: Double,
    val xScalarToAdd: Double,
    val yScalarToAdd: Double,
) : VersionedEntity {
    fun toType(
        callouts: List<MapCallout>,
    ): GameMap {
        return GameMap(
            uuid,
            displayName,
            narrativeDescription,
            tacticalDescription,
            coordinates,
            displayIcon,
            listViewIcon,
            listViewIconTall,
            splash,
            stylizedBackgroundImage,
            premierBackgroundImage,
            type,
            xMultiplier,
            yMultiplier,
            xScalarToAdd,
            yScalarToAdd,
            callouts
        )
    }
}