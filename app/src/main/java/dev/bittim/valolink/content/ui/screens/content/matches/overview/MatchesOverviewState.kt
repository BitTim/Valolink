/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesOverviewState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.matches.overview

data class MatchesOverviewState(
    val isLoading: Boolean = false,
    val matchGroups: List<Any> = emptyList<Any>()
)
