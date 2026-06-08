/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapCategory.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 19:56
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
    }
}