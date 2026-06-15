/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FormatScoreUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 14:50
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.ValoModeCategory
import dev.bittim.valolink.core.ui.toOrdinal

class FormatScoreUseCase {
    operator fun invoke(scoreA: Int?, scoreB: Int?, modeCategory: ValoModeCategory): String? {
        return when(modeCategory) {
            ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range -> null
            ValoModeCategory.Standard, ValoModeCategory.TDM, ValoModeCategory.Skirmish -> {
                "${scoreA ?: '?'} - ${scoreB ?: '?'}"
            }
            ValoModeCategory.Deathmatch -> scoreA?.toOrdinal() ?: "?"
        }
    }
}