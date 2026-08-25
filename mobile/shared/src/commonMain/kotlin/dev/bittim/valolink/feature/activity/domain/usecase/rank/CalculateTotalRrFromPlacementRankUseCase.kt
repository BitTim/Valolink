/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateTotalRrFromPlacementRankUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.08.26, 12:19
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants

class CalculateTotalRrFromPlacementRankUseCase(
    private val filterRanksUseCase: FilterRanksUseCase
) {
    /**
     * Calculates the total rating points for a placement rank tier.
     *
     * @param placementRankTier The tier assigned to the placement rank.
     * @param ranks The ranks used to determine the minimum tier.
     * @return The calculated rating points, or `null` if no ranks remain after filtering.
     */
    operator fun invoke(placementRankTier: Int, ranks: List<ValoRank>): Int? {
        val tierOffset = filterRanksUseCase(ranks).minOfOrNull { it.tier } ?: return null
        return (placementRankTier - tierOffset) * RankConstants.RR_PER_RANK + (RankConstants.RR_PER_RANK / 2)
    }
}