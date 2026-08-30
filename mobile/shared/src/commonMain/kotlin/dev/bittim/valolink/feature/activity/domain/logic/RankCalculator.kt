/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankCalculator.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.08.26, 03:29
 */

package dev.bittim.valolink.feature.activity.domain.logic

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants
import dev.bittim.valolink.feature.activity.domain.model.RankChange
import kotlin.math.floor
import kotlin.time.Instant
import kotlin.uuid.Uuid

object RankCalculator {
    /**
     * Calculates the sum of `rr` values for matching activities up to a given timestamp.
     *
     * @param activities The activities to process.
     * @param modeUuid The mode to match against each activity's mode.
     * @param time The latest timestamp to include.
     * @return The sum of `rr` values for activities with a matching mode and `time` less than or equal to `before`, or `null` if no matching activities contribute to the sum.
     */
    fun calculateRrUpToTime(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        time: Instant,
        inclusive: Boolean = false
    ): Int? {
        if (activities == null) return null

        var totalRr = 0
        var hasRr = false

        for(activity in activities) {
            val (activityModeUuid, activityRr) = when (activity) {
                is Activity.MatchActivity -> activity.match.mode.uuid to activity.rr
                is Activity.RrRefundActivity -> activity.mode.uuid to activity.rr
                else -> continue
            }

            if (activityModeUuid == modeUuid && ((inclusive && activity.time <= time) || (!inclusive && activity.time < time)) && activityRr != null) {
                totalRr += activityRr
                hasRr = true
            }
        }

        return totalRr.takeIf { hasRr }
    }

    fun calculateRankChanges(
        activities: List<Activity>?,
        ranks: List<ValoRank>?
    ): Map<Uuid, RankChange>? {
        if (activities == null) return null

        val result = mutableMapOf<Uuid, RankChange>()
        val runningRrByMode = mutableMapOf<Uuid, Int?>()

        for (activity in activities.sortedBy { it.time }) {
            val (modeUuid, rrDelta) = when (activity) {
                is Activity.MatchActivity -> {
                    if (!activity.match.isRanked) continue
                    activity.match.mode.uuid to activity.rr
                }
                is Activity.RrRefundActivity -> activity.mode.uuid to activity.rr
                else -> continue
            }

            val runningRr = runningRrByMode[modeUuid]
            check(!(runningRr != null && rrDelta == null)) {
                "Ranked match with no rr found after a placement was already established for mode $modeUuid"
            }

            result[activity.id] = RankChange.fromFinalRr(runningRr, rrDelta, ranks)
            runningRrByMode[modeUuid] = when {
                runningRr == null && rrDelta == null -> null
                else -> (runningRr ?: 0) + (rrDelta ?: 0)
            }
        }

        return result
    }

    /**
     * Calculates the stored RR change from a rank's current RR, visible RR change, and rank modifier status.
     *
     * @param rank The rank whose current RR determines tier boundary adjustments.
     * @param visibleRr The displayed RR change.
     * @param rankModifier Whether the applicable rank modifier is active.
     * @return The adjusted RR delta, including rank shield compensation or double rank-up adjustment when applicable.
     */
    fun calculateRrDelta(rank: Rank, visibleRr: Int, rankModifier: Boolean): Int {
        val combinedRr = rank.rr + visibleRr

        val modifierRr = when {
            // Rank Shield was used, negative RR will have no effect
            visibleRr < 0 && rank.rr == 0 && rankModifier -> -visibleRr
            // Double rank up happened
            visibleRr > 0 && combinedRr >= RankConstants.RR_PER_RANK && rankModifier -> RankConstants.RR_PER_RANK
            else -> 0
        }

        val rr = when {
            // When losing RR while having more than 0 within a tier, the owned RR is capped at 0
            combinedRr < 0 && rank.rr > 0 -> -rank.rr

            // When gaining RR and going over the rank up threshold, check if the RR within the new tier are at least the minimum amount after a rank up.
            // If not, add the difference to the RR, so it would land at the minimum amount after a rank up.
            combinedRr >= RankConstants.RR_PER_RANK && combinedRr - RankConstants.RR_PER_RANK < RankConstants.RANK_UP_MIN_RR ->
                RankConstants.RR_PER_RANK + RankConstants.RANK_UP_MIN_RR - combinedRr + visibleRr

            // If not other cases apply, the visible RR is already accurate
            else -> visibleRr
        }

        return rr + modifierRr
    }

    /**
     * Calculates the total rating points for a placement rank tier.
     *
     * @param placementRankTier The tier assigned to the placement rank.
     * @param placementRr The placement RR value.
     * @param ranks The ranks used to determine the minimum tier.
     * @return The calculated rating points, or `null` if no ranks remain after filtering.
     */
    fun calculateTotalRrFromPlacement(placementRankTier: Int, placementRr: Int, ranks: List<ValoRank>): Int? {
        val tierOffset = RankFilter.filterUnrankedAndInvalid(ranks).minOfOrNull { it.tier } ?: return null
        return (placementRankTier - tierOffset) * RankConstants.RR_PER_RANK + placementRr
    }

    /**
     * Maps a competitive rating value to the corresponding ranked tier.
     *
     * @param rr The RR value, or `null` to resolve the tier-0 rank with zero RR.
     * @param ranks The rank definitions for the active competitive season.
     * @return The resolved rank with its remaining RR, or `null` when no applicable ranked tier exists.
     */
    fun mapRrToRank(rr: Int?, ranks: List<ValoRank>): Rank? {
        if (rr == null) return ranks.firstOrNull { it.tier == 0 }?.let {
            Rank(rank = it, rr = 0)
        }

        val rankedRanks = RankFilter.filterUnrankedAndInvalid(ranks)
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