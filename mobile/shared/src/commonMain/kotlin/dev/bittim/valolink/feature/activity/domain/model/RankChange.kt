/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankChange.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:31
 */

package dev.bittim.valolink.feature.activity.domain.model

import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.logic.RankCalculator

data class RankChange(
    val current: Rank? = null,
    val new: Rank? = null,
    val rrDelta: Int? = null,
) {
    companion object {
        fun fromFinalRr(totalRr: Int?, rrDelta: Int?, ranks: List<ValoRank>?): RankChange {
            val rankDefinitions = ranks ?: return RankChange()
            val currentRank = RankCalculator.mapRrToRank(totalRr, rankDefinitions)
            val newRank = if (rrDelta == null) currentRank else RankCalculator.mapRrToRank((totalRr ?: 0) + rrDelta, rankDefinitions)
            return RankChange(currentRank, newRank, rrDelta)
        }

        /**
         * Calculates the rank change for a player with an existing rating.
         *
         * @param totalRr The player's total rating before the activity.
         * @param visibleRrDelta The visible rating change from the activity.
         * @param rankModifier Whether the rank-based rating modifier applies.
         * @param ranks The rank definitions used to map rating values to ranks.
         * @return The calculated rank change, or an empty result when rank definitions are unavailable.
         */
        fun fromRawRr(
            totalRr: Int,
            visibleRrDelta: Int?,
            rankModifier: Boolean,
            ranks: List<ValoRank>?,
        ): RankChange {
            val rankDefinitions = ranks ?: return RankChange()
            val currentRank = RankCalculator.mapRrToRank(totalRr, rankDefinitions)
            val rrDelta = visibleRrDelta?.let { rr ->
                currentRank?.let { RankCalculator.calculateRrDelta(it, rr, rankModifier) }
            }
            return fromFinalRr(totalRr, rrDelta, rankDefinitions)
        }

        /**
         * Calculates the rank change for a placement scenario.
         *
         * @param placement Whether placement is active.
         * @param selectedRankTier The selected rank tier for placement.
         * @param placementRr The placement RR value.
         * @param ranks The available rank definitions.
         * @return The unranked current rank, resulting placement rank, and placement RR delta, or an empty change when rank definitions are unavailable.
         */
        fun fromPlacement(
            placement: Boolean,
            selectedRankTier: Int?,
            placementRr: Int?,
            ranks: List<ValoRank>?,
        ): RankChange {
            val rankDefinitions = ranks ?: return RankChange()
            val rrDelta = if(placement && selectedRankTier != null && placementRr != null) {
                RankCalculator.calculateTotalRrFromPlacement(selectedRankTier, placementRr, rankDefinitions)
            } else null

            return fromFinalRr(null, rrDelta, ranks)
        }
    }
}
