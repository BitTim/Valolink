/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankCardState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   21.07.26, 15:44
 */

package dev.bittim.valolink.feature.activity.ui.components.rank

import androidx.compose.ui.graphics.Color
import dev.bittim.valolink.core.domain.extension.ColorFormat
import dev.bittim.valolink.core.domain.extension.parseColor
import dev.bittim.valolink.core.domain.model.ValoRank

data class RankCardState(
    val tier: Int = 0,
    val name: String = "",
    val division: String = "",
    val imageUrl: String? = null,
    val color: Color = Color.Transparent,
    val backgroundColor: Color = Color.Transparent
) {
    companion object {
        /**
         * Creates rank-card state from rank metadata and colors.
         *
         * @param rank The rank data used to populate the state.
         * @return A rank-card state containing the rank details and parsed colors.
         */
        fun from(rank: ValoRank): RankCardState {
            return RankCardState(
                tier = rank.tier,
                name = rank.tierName,
                division = rank.divisionName,
                imageUrl = rank.largeIcon,
                color = Color.parseColor(rank.color, ColorFormat.RGBA),
                backgroundColor = Color.parseColor(rank.backgroundColor, ColorFormat.RGBA)
            )
        }
    }
}
