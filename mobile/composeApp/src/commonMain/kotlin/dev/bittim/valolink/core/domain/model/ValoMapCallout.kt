/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapCallout.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:01
 */

package dev.bittim.valolink.core.domain.model

data class ValoMapCallout(
    val regionName: Map<String, String>,
    val superRegion: String,
    val superRegionName: Map<String, String>,
    val location: Location,
    val scale3D: Location?,
    val rotation: Rotation?
)
