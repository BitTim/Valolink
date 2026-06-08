/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 21:46
 */

package dev.bittim.valolink.feature.activity.ui.components.map

import dev.bittim.valolink.core.domain.model.SimpleValoMap
import kotlin.uuid.Uuid

data class MapCardState(
    val uuid: Uuid,
    val title: String,
    val coordinates: String?,
    val imageUrl: String?
) {
    companion object {
        fun from(map: SimpleValoMap): MapCardState {
            return MapCardState(
                uuid = map.uuid,
                title = map.displayName,
                coordinates = map.coordinates,
                imageUrl = map.splash
            )
        }
    }
}
