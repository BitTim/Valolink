/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SimpleValoMap.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 19:56
 */

package dev.bittim.valolink.core.domain.model

import kotlin.uuid.Uuid

data class SimpleValoMap(
    val uuid: Uuid,
    val displayName: String,
    val coordinates: String?,
    val category: ValoMapCategory,
    val listViewIcon: String,
    val listViewIconTall: String,
    val splash: String,
    val premierBackgroundImage: String?,
    val stylizedBackgroundImage: String?,
)
