/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.06.26, 01:41
 */

package dev.bittim.valolink.feature.activity.ui.components.rank

import dev.bittim.valolink.core.domain.model.ValoRank

data class RankCardState(
    val name: String? = null,
) {
    companion object {
        fun from(rank: ValoRank): RankCardState {
            return RankCardState(
                name = rank.tierName,
            )
        }
    }
}
