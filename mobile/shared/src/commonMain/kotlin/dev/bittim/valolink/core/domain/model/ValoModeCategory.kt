/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeCategory.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 19:52
 */

package dev.bittim.valolink.core.domain.model

enum class ValoModeCategory {
    Unknown,
    Tutorial,
    Range,
    Standard,
    Deathmatch,
    TDM,
    Skirmish;

    enum class ScoreType {
        Default,
        Placement
    }

    fun getScoreType(): ScoreType {
        return when (this) {
            Deathmatch -> ScoreType.Placement
            else -> ScoreType.Default
        }
    }

    companion object {
        fun parse(value: String): ValoModeCategory {
            return when (value.lowercase()) {
                "unknown" -> Unknown
                "tutorial" -> Tutorial
                "range" -> Range
                "standard" -> Standard
                "deathmatch" -> Deathmatch
                "tdm" -> TDM
                "skirmish" -> Skirmish
                else -> Unknown
            }
        }
    }
}