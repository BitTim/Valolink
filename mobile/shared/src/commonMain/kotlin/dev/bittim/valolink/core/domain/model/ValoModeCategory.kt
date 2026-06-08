/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeCategory.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 21:09
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