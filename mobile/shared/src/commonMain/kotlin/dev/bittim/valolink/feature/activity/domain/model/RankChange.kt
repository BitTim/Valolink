/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankChange.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:58
 */

package dev.bittim.valolink.feature.activity.domain.model

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.logic.RankCalculator
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class RankChange(
    val current: Rank? = null,
    val new: Rank? = null,
    val rrDelta: Int? = null,
) {
    companion object {
        /**
         * Calculates the current and new rank for a ranked activity.
         *
         * @param activities Activities used to determine the rating before the specified time.
         * @param modeUuid The game mode identifier.
         * @param time The point in time before which the rating is calculated.
         * @param visibleRrDelta The visible RR change to apply.
         * @param rankModifier Whether the rank modifier applies to the RR change.
         * @param placement Whether the calculation is for placement.
         * @param selectedRankTier The rank tier selected for placement.
         * @param ranks Available rank definitions.
         * @return The current rank, new rank, and RR change.
         */
        fun calculate(
            activities: List<Activity>?,
            modeUuid: Uuid?,
            time: Instant,
            visibleRrDelta: Int?,
            rankModifier: Boolean,
            placement: Boolean,
            selectedRankTier: Int?,
            ranks: List<ValoRank>?,
        ): RankChange {
            val totalRr = RankCalculator.calculateRrUpToTime(activities, modeUuid, time)
            return if (totalRr != null) {
                calculateExistingRankChange(
                    totalRr = totalRr,
                    visibleRrDelta = visibleRrDelta,
                    rankModifier = rankModifier,
                    ranks = ranks,
                )
            } else {
                calculatePlacementRankChange(
                    placement = placement,
                    selectedRankTier = selectedRankTier,
                    ranks = ranks,
                )
            }
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
        private fun calculateExistingRankChange(
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
            val newRank = RankCalculator.mapRrToRank(totalRr + (rrDelta ?: 0), rankDefinitions)

            return RankChange(
                current = currentRank,
                new = newRank,
                rrDelta = rrDelta,
            )
        }

        /**
         * Calculates the rank change for a placement scenario.
         *
         * @param placement Whether placement is active.
         * @param selectedRankTier The selected rank tier for placement.
         * @param ranks The available rank definitions.
         * @return The unranked current rank, resulting placement rank, and placement RR delta, or an empty change when rank definitions are unavailable.
         */
        private fun calculatePlacementRankChange(
            placement: Boolean,
            selectedRankTier: Int?,
            ranks: List<ValoRank>?,
        ): RankChange {
            val rankDefinitions = ranks ?: return RankChange()
            val unranked = RankCalculator.mapRrToRank(null, rankDefinitions)
            val newRank = if (placement && selectedRankTier != null) {
                rankDefinitions.find { it.tier == selectedRankTier }?.let { Rank(rank = it, rr = 50) } ?: unranked
            } else {
                unranked
            }

            val rrDelta = if(placement && selectedRankTier != null) {
                RankCalculator.calculateTotalRrFromPlacement(selectedRankTier, rankDefinitions)
            } else 0

            return RankChange(
                current = unranked,
                new = newRank,
                rrDelta = rrDelta,
            )
        }
    }
}
