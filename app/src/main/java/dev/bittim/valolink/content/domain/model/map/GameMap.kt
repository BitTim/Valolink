/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       GameMap.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.domain.model.map

data class GameMap(
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
    val type: MapType,
    val xMultiplier: Double,
    val yMultiplier: Double,
    val xScalarToAdd: Double,
    val yScalarToAdd: Double,
    val callouts: List<MapCallout>
)
