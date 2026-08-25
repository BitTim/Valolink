/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       CalculateRrDeltaUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 16:54
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants

class CalculateRrDeltaUseCase {
    /**
     * Calculates the stored RR change from a rank's current RR, visible RR change, and rank modifier status.
     *
     * @param rank The rank whose current RR determines tier boundary adjustments.
     * @param visibleRr The displayed RR change.
     * @param rankModifier Whether the applicable rank modifier is active.
     * @return The adjusted RR delta, including rank shield compensation or double rank-up adjustment when applicable.
     */
    operator fun invoke(rank: Rank, visibleRr: Int, rankModifier: Boolean): Int {
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
}