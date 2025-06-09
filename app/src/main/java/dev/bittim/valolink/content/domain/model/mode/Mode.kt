/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Mode.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.domain.model.mode

data class Mode(
    val uuid: String,
    val displayName: String,
    val description: String?,
    val scoreType: ScoreType,
    val canBeRanked: Boolean,
    val duration: String?,
    val roundsPerHalf: Int,
    val displayIcon: String?,
    val listViewIconTall: String?
)