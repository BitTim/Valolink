/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapCategory.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 19:43
 */

package dev.bittim.valolink.core.domain.model

enum class ValoMapCategory {
    Unknown,
    Tutorial,
    Range,
    Standard,
    TDM,
    Skirmish;

    companion object {
        fun parse(value: String): ValoMapCategory {
            return when (value.lowercase()) {
                "tutorial" -> Tutorial
                "range" -> Range
                "standard" -> Standard
                "tdm" -> TDM
                "skirmish" -> Skirmish
                else -> Unknown
            }
        }

        fun from(modeCategory: ValoModeCategory): ValoMapCategory {
            return when (modeCategory) {
                ValoModeCategory.Unknown -> Unknown
                ValoModeCategory.Tutorial -> Tutorial
                ValoModeCategory.Range -> Range
                ValoModeCategory.Standard -> Standard
                ValoModeCategory.Deathmatch -> Standard
                ValoModeCategory.TDM -> TDM
                ValoModeCategory.Skirmish -> Skirmish
            }
        }
    }
}