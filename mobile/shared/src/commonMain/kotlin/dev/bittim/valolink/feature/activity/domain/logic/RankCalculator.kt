/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankCalculator.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:55
 */

package dev.bittim.valolink.feature.activity.domain.logic

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants
import kotlin.math.floor
import kotlin.time.Instant
import kotlin.uuid.Uuid

object RankCalculator {
    /**
     * Calculates the sum of `rr` values for matching activities up to a given timestamp.
     *
     * @param activities The activities to process.
     * @param modeUuid The mode to match against each activity's mode.
     * @param before The latest timestamp to include.
     * @return The sum of `rr` values for activities with a matching mode and `time` less than or equal to `before`, or `null` if no matching activities contribute to the sum.
     */
    fun calculateRrUpToTime(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        before: Instant
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

            if (activityModeUuid == modeUuid && activity.time <= before && activityRr != null) {
                totalRr += activityRr
                hasRr = true
            }
        }

        return totalRr.takeIf { hasRr }
    }

    /**
     * Calculates the sum of `rr` values for matching activities up to a specified identifier.
     *
     * @param activities The activities to process.
     * @param modeUuid The activity mode to match.
     * @param upToInclusive The activity identifier to include up to.
     * @return The sum of `rr` values for matching activities up to `upToInclusive`, or `null` if no matching `rr` values are found or `activities` is `null`.
     */
    fun calculateRrUpToId(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        upToInclusive: Uuid
    ): Int? {
        if (activities == null) return null

        val sortedActivities = activities.filter {
            val activityModeUuid = when(it) {
                is Activity.MatchActivity -> it.match.mode.uuid
                is Activity.RrRefundActivity -> it.mode.uuid
                else -> false
            }

            activityModeUuid == modeUuid
        }.sortedBy { it.time }
        val lastIndex = sortedActivities.indexOfFirst { it.id == upToInclusive }
        val filteredActivities = sortedActivities.take(lastIndex + 1).filter {
            when(it) {
                is Activity.MatchActivity -> it.rr != null
                is Activity.RrRefundActivity -> true
                else -> false
            }
        }
        return if (filteredActivities.isEmpty()) null else filteredActivities.sumOf {
            when(it) {
                is Activity.MatchActivity -> it.rr!!
                is Activity.RrRefundActivity -> it.rr
                else -> 0
            }
        }
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
     * @param ranks The ranks used to determine the minimum tier.
     * @return The calculated rating points, or `null` if no ranks remain after filtering.
     */
    fun calculateTotalRrFromPlacement(placementRankTier: Int, ranks: List<ValoRank>): Int? {
        val tierOffset = RankFilter.filterUnrankedAndInvalid(ranks).minOfOrNull { it.tier } ?: return null
        return (placementRankTier - tierOffset) * RankConstants.RR_PER_RANK + (RankConstants.RR_PER_RANK / 2)
    }

    /**
     * Maps a competitive rating value to the corresponding ranked tier.
     *
     * @param rr The RR value, or `null` to resolve the tier-0 rank with zero RR.
     * @param ranks The rank definitions for the active competitive season.
     * @return The resolved rank with its remaining RR, or `null` when no applicable ranked tier exists.
     */
    fun mapRrToRank(rr: Int?, ranks: List<ValoRank>): Rank? {
        val rankedRanks = RankFilter.filterUnrankedAndInvalid(ranks)

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