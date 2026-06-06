/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMode.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:32
 */

package dev.bittim.valolink.core.domain.model

import kotlin.uuid.Uuid

data class ValoMode(
    val uuid: Uuid,
    val displayName: String,
    val description: String?,
    val duration: String?,
    val category: String,
    val displayIcon: String?,
    val listViewIconTall: String?,
    val roundsPerHalf: Int,
    val canBeRanked: Boolean
)
