/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       DetermineScoreResultUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.06.25, 16:00
 */

package dev.bittim.valolink.core.domain.usecase.score

import dev.bittim.valolink.core.domain.model.ScoreResult
import javax.inject.Inject

class DetermineScoreResultUseCase @Inject constructor() {
    operator fun invoke(
        score: Int,
        enemyScore: Int?,
        surrender: Boolean,
        enemySurrender: Boolean,
    ): ScoreResult {
        // Placement scoring
        if (enemyScore == null) {
            if (score == 1) return ScoreResult.Win
            return ScoreResult.Draw
        }

        // Default Scoring
        if (surrender) return ScoreResult.Loss
        if (enemySurrender) return ScoreResult.Win
        if (score > enemyScore) return ScoreResult.Win
        if (score < enemyScore) return ScoreResult.Loss
        return ScoreResult.Draw
    }
}