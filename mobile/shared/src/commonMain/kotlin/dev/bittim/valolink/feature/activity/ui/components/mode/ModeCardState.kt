/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ModeCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 19:33
 */

package dev.bittim.valolink.feature.activity.ui.components.mode

import kotlin.uuid.Uuid

data class ModeCardState(
    val uuid: Uuid,
    val iconUrl: String?,
    val title: String,
    val duration: String?,
    val canBeRanked: Boolean
)
