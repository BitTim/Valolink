/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       GameFeatureOverrideDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.remote.dto.mode

data class GameFeatureOverrideDto(
    val featureName: String,
    val state: Boolean,
)
