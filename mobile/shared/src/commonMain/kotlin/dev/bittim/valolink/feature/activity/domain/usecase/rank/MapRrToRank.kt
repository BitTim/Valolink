/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapRrToRank.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 12:58
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants
import kotlin.math.floor

class MapRrToRank {
    /**
     * Maps an RR value to the matching rank for the active season at the given time.
     *
     * @param rr The RR value to resolve, or `null` to return the tier-0 rank.
     * @param ranks The rank definitions for the active competitive season.
     * @return The resolved `Rank`, or `null` if the rank definitions do not contain a match.
     */
    operator fun invoke(rr: Int?, ranks: List<ValoRank>): Rank? {
        val rankedRanks = ranks.filter {
            !it.division.contains("UNRANKED") && !it.division.contains("INVALID")
        }

        if (rr == null) return ranks.firstOrNull { it.tier == 0 }?.let {
            Rank(rank = it, rr = 0)
        }

        val tierOffset = rankedRanks.minOfOrNull { it.tier } ?: return null
        val relativeTier = floor(rr.toDouble() / RankConstants.RR_PER_RANK).toInt()
        val calculatedTier = relativeTier + tierOffset
        val calculatedRr = rr - relativeTier * RankConstants.RR_PER_RANK

        val actualTier = calculatedTier.coerceAtMost(rankedRanks.lastOrNull()?.tier ?: 0)
        val rank = rankedRanks.firstOrNull { it.tier == actualTier } ?: return null

        return Rank(
            rank = rank,
            rr = calculatedRr
        )
    }
}
