/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRankChangeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 13:06
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
    private val mapRrToRank: MapRrToRank,
    private val calculateRrDeltaUseCase: CalculateRrDeltaUseCase,
) {
    operator fun invoke(
        activities: List<Activity>?,
        modeUuid: Uuid?,
        time: Instant,
        visibleRrDelta: Int?,
        placement: Boolean,
        selectedRankTier: Int?,
        ranks: List<ValoRank>?,
    ): RankChange {
        val totalRr = calculateRrBeforeTimeUseCase(activities, modeUuid, time)
        return if (totalRr != null) {
            calculateExistingRankChange(
                totalRr = totalRr,
                visibleRrDelta = visibleRrDelta,
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

    private fun calculateExistingRankChange(
        totalRr: Int,
        visibleRrDelta: Int?,
        ranks: List<ValoRank>?,
    ): RankChange {
        val rankDefinitions = ranks ?: return RankChange()
        val currentRank = mapRrToRank(totalRr, rankDefinitions)
        val rrDelta = visibleRrDelta?.let { rr ->
            currentRank?.let { calculateRrDeltaUseCase(it, rr) }
        }
        val newRank = mapRrToRank(totalRr + (rrDelta ?: 0), rankDefinitions)

        return RankChange(
            current = currentRank,
            new = newRank,
            rrDelta = rrDelta,
        )
    }

    private fun calculatePlacementRankChange(
        placement: Boolean,
        selectedRankTier: Int?,
        ranks: List<ValoRank>?,
    ): RankChange {
        val rankDefinitions = ranks ?: return RankChange()
        val unranked = mapRrToRank(null, rankDefinitions)
        val newRank = if (placement && selectedRankTier != null) {
            rankDefinitions.find { it.tier == selectedRankTier }?.let { Rank(rank = it, rr = 50) } ?: unranked
        } else {
            unranked
        }

        return RankChange(
            current = unranked,
            new = newRank,
        )
    }
}
