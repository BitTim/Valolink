/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankCardData.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   06.07.25, 02:52
 */

package dev.bittim.valolink.core.ui.components.rankCard

data class RankCardData(
    val rankName: String,
    val rankIcon: String?,
    val gradient: List<String>,
)