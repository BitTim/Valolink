/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapCalloutDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.remote.dto.map

import dev.bittim.valolink.content.data.local.entity.map.MapCalloutEntity

data class MapCalloutDto(
    val regionName: String,
    val superRegionName: String,
    val location: CalloutLocationDto
) {
    fun toEntity(
        version: String,
        mapUuid: String,
    ): MapCalloutEntity {
        return MapCalloutEntity(
            mapUuid + "_" + regionName + "_" + superRegionName,
            mapUuid,
            version,
            regionName,
            superRegionName,
            location.x,
            location.y
        )
    }
}
