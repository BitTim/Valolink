/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       DetermineRankChangeResultUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 22:18
 */

package dev.bittim.valolink.core.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.RankChangeResult
import javax.inject.Inject

class DetermineRankChangeResultUseCase @Inject constructor() {
    companion object {
        const val MIN_TIER = 3
        const val MAX_RR = 100
    }

    operator fun invoke(
        tier: Int,
        rr: Int,
        deltaRR: Int,
        specialBehavior: Boolean,
        maxTier: Int,
    ): RankChangeResult {
        var newRR = rr + deltaRR
        var newTier = tier

        if (specialBehavior) {
            when {
                deltaRR > 0 -> newTier += 1
                deltaRR < 0 -> newRR = 0
            }
        }

        if (newRR >= MAX_RR && tier < maxTier - 1) {
            newRR -= MAX_RR
            if (newRR < 10) newRR = 10

            newTier += 1
        } else if (newRR < 0) {
            if (rr == 0) newTier -= 1 else newRR = 0
        }

        if (newTier < MIN_TIER) newTier = MIN_TIER
        if (newTier > maxTier - 1) newTier = maxTier - 1

        val shadowDeltaRR = ((newTier - tier) * MAX_RR) + (newRR - rr)

        return RankChangeResult(
            newTier = newTier,
            newRR = newRR,
            shadowDeltaRR = shadowDeltaRR,
        )
    }
}