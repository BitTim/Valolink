/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       GameRuleBoolOverrideDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.remote.dto.mode

data class GameRuleBoolOverrideDto(
    val ruleName: String,
    val state: Boolean,
)
