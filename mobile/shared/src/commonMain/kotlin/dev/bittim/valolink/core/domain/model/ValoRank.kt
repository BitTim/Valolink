/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRank.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:35
 */

package dev.bittim.valolink.core.domain.model

import kotlin.uuid.Uuid

data class ValoRank(
    val rankTable: Uuid,
    val tier: Int,
    val tierName: String,
    val smallIcon: String?,
    val largeIcon: String?,
    val rankTriangleDownIcon: String?,
    val rankTriangleUpIcon: String?,
    val color: String,
    val backgroundColor: String,
    val division: String,
    val divisionName: String
)
