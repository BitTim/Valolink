/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapCallout.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.embedded

import kotlinx.serialization.Serializable

@Serializable
data class ValoMapCallout(
    val regionName: Map<String, String>,
    val superRegion: String,
    val superRegionName: Map<String, String>,
    val location: Location,
    val scale3D: Location?,
    val rotation: Rotation?
)
