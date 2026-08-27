/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FormatScore.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 20:01
 */

package dev.bittim.valolink.feature.activity.domain.logic

import dev.bittim.valolink.core.domain.model.ValoModeCategory
import dev.bittim.valolink.core.ui.toOrdinal

fun formatScore(scoreA: Int?, scoreB: Int?, modeCategory: ValoModeCategory, isTeamB: Boolean = false): String {
    val left = if(isTeamB) scoreB else scoreA
    val right = if(isTeamB) scoreA else scoreB

    return when(modeCategory) {
        ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range -> "?"
        ValoModeCategory.Standard, ValoModeCategory.TDM, ValoModeCategory.Skirmish -> {
            "${left ?: '?'} - ${right ?: '?'}"
        }
        ValoModeCategory.Deathmatch -> scoreA?.toOrdinal() ?: "?"
    }
}