/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRankChangeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.08.26, 14:01
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.model.ValoRank
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class RankChange(
    val current: Rank? = null,
    val new: Rank? = null,
    val rrDelta: Int? = null,
)

/** Calculates the current rank, RR change, and resulting rank for a ranked activity. */
class CalculateRankChangeUseCase(
    private val calculateRrBeforeTimeUseCase: CalculateRrBeforeTimeUseCase,
    private val mapRrToRankUseCase: MapRrToRankUseCase,
    private val calculateRrDeltaUseCase: CalculateRrDeltaUseCase,
    private val calculateTotalRrFromPlacementRankUseCase: CalculateTotalRrFromPlacementRankUseCase,
) {
    /**
     * Calculates the rank change for a ranked activity based on prior rating or placement status.
     *
     * @param activities Activities used to determine the rating before the specified time.
     * @param modeUuid The game mode identifier.
     * @param time The point in time before which the rating is calculated.
     * @param visibleRrDelta The visible rating change to apply.
     * @param placement Whether the calculation is for placement.
     * @param selectedRankTier The rank tier selected for placement.
     * @param ranks Available rank definitions.
     * @return The current rank, new rank, and rating change.
     */
    operator fun invoke(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        time: Instant,
        visibleRrDelta: Int?,
        rankModifier: Boolean,
        placement: Boolean,
        selectedRankTier: Int?,
        ranks: List<ValoRank>?,
    ): RankChange {
        val totalRr = calculateRrBeforeTimeUseCase(activities, modeUuid, time)
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
     * Calculates the current rank, RR change, and resulting rank for an existing player.
     *
     * @param totalRr The player's total RR before the activity.
     * @param visibleRrDelta The visible RR change from the activity.
     * @param ranks The rank definitions used to map RR values to ranks.
     * @return The calculated rank change, or an empty result when rank definitions are unavailable.
     */
    private fun calculateExistingRankChange(
        totalRr: Int,
        visibleRrDelta: Int?,
        rankModifier: Boolean,
        ranks: List<ValoRank>?,
    ): RankChange {
        val rankDefinitions = ranks ?: return RankChange()
        val currentRank = mapRrToRankUseCase(totalRr, rankDefinitions)
        val rrDelta = visibleRrDelta?.let { rr ->
            currentRank?.let { calculateRrDeltaUseCase(it, rr, rankModifier) }
        }
        val newRank = mapRrToRankUseCase(totalRr + (rrDelta ?: 0), rankDefinitions)

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
     * @return The unranked current rank and the resulting placement rank, or an empty change when rank definitions are unavailable.
     */
    private fun calculatePlacementRankChange(
        placement: Boolean,
        selectedRankTier: Int?,
        ranks: List<ValoRank>?,
    ): RankChange {
        val rankDefinitions = ranks ?: return RankChange()
        val unranked = mapRrToRankUseCase(null, rankDefinitions)
        val newRank = if (placement && selectedRankTier != null) {
            rankDefinitions.find { it.tier == selectedRankTier }?.let { Rank(rank = it, rr = 50) } ?: unranked
        } else {
            unranked
        }

        val rrDelta = if(placement && selectedRankTier != null) {
            calculateTotalRrFromPlacementRankUseCase(selectedRankTier, rankDefinitions)
        } else 0

        return RankChange(
            current = unranked,
            new = newRank,
            rrDelta = rrDelta,
        )
    }
}
