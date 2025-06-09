/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapCallout.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.domain.model.map

data class MapCallout(
    val map: String,
    val regionName: String?,
    val superRegionName: String?,
    val x: Double,
    val y: Double,
)
