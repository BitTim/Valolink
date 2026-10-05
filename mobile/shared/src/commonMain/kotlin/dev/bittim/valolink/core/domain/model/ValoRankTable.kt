/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankTable.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   26.09.26, 17:16
 */

package dev.bittim.valolink.core.domain.model

import kotlin.uuid.Uuid

data class ValoRankTable(
    val uuid: Uuid,
    val ranks: List<ValoRank>
)
