/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 21:25
 */

package dev.bittim.valolink.feature.activity.ui.components.mode

import dev.bittim.valolink.core.domain.model.ValoMode
import kotlin.uuid.Uuid

data class ModeCardState(
    val uuid: Uuid,
    val iconUrl: String?,
    val title: String,
    val duration: String?,
    val canBeRanked: Boolean
) {
    companion object {
        fun from(mode: ValoMode) = ModeCardState(
            uuid = mode.uuid,
            iconUrl = mode.displayIcon,
            title = mode.displayName,
            duration = mode.duration,
            canBeRanked = mode.canBeRanked
        )
    }
}
