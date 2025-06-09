/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.remote.dto.map

import dev.bittim.valolink.content.data.local.entity.map.MapEntity
import dev.bittim.valolink.content.domain.model.map.MapType

data class MapDto(
    val uuid: String,
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
    val assetPath: String,
    val mapUrl: String,
    val xMultiplier: Double,
    val yMultiplier: Double,
    val xScalarToAdd: Double,
    val yScalarToAdd: Double,
    val callouts: List<MapCalloutDto>?
) {
    fun toEntity(version: String): MapEntity {
        val type = when {
            mapUrl.contains("HURM") -> MapType.TDM
            mapUrl.contains("Range") -> MapType.Range
            mapUrl.contains("NPEV2") -> MapType.Tutorial
            else -> MapType.Default
        }

        return MapEntity(
            uuid,
            version,
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
            yScalarToAdd
        )
    }
}
