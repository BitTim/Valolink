/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Mode.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.06.25, 01:14
 */

package dev.bittim.valolink.content.domain.model.mode

import dev.bittim.valolink.content.domain.model.map.MapType

data class Mode(
    val uuid: String,
    val displayName: String,
    val description: String?,
    val scoreType: ScoreType,
    val mapType: MapType,
    val canBeRanked: Boolean,
    val duration: String?,
    val roundsPerHalf: Int,
    val displayIcon: String?,
    val listViewIconTall: String?
)