/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapRrToRankUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.08.26, 12:14
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants
import kotlin.math.floor

class MapRrToRankUseCase(
    private val filterRanksUseCase: FilterRanksUseCase
) {
    /**
     * Maps a competitive rating value to the corresponding ranked tier.
     *
     * @param rr The RR value, or `null` to resolve the tier-0 rank with zero RR.
     * @param ranks The rank definitions for the active competitive season.
     * @return The resolved rank with its remaining RR, or `null` when no applicable ranked tier exists.
     */
    operator fun invoke(rr: Int?, ranks: List<ValoRank>): Rank? {
        val rankedRanks = filterRanksUseCase(ranks)

        if (rr == null) return ranks.firstOrNull { it.tier == 0 }?.let {
            Rank(rank = it, rr = 0)
        }

        val boundedRr = rr.coerceAtLeast(0)
        val tierOffset = rankedRanks.minOfOrNull { it.tier } ?: return null
        val highestTier = rankedRanks.maxOf { it.tier }
        val relativeTier = floor(boundedRr.toDouble() / RankConstants.RR_PER_RANK).toInt()
        val calculatedTier = relativeTier + tierOffset
        val calculatedRr = boundedRr - relativeTier * RankConstants.RR_PER_RANK

        val actualTier = calculatedTier.coerceIn(tierOffset, highestTier)
        val rank = rankedRanks.firstOrNull { it.tier == actualTier } ?: return null

        return Rank(
            rank = rank,
            rr = calculatedRr
        )
    }
}
