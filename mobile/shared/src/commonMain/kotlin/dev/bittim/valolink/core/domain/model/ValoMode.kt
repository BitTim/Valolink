/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMode.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 21:09
 */

package dev.bittim.valolink.core.domain.model

import kotlin.uuid.Uuid

data class ValoMode(
    val uuid: Uuid,
    val displayName: String,
    val description: String?,
    val duration: String?,
    val category: ValoModeCategory,
    val displayIcon: String?,
    val listViewIconTall: String?,
    val roundsPerHalf: Int,
    val canBeRanked: Boolean
)
