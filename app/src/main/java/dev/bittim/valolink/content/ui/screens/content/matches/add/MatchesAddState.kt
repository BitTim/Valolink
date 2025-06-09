/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesAddState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 21:31
 */

package dev.bittim.valolink.content.ui.screens.content.matches.add

import dev.bittim.valolink.content.domain.model.map.GameMap
import dev.bittim.valolink.content.domain.model.mode.Mode
import dev.bittim.valolink.content.domain.model.rank.Rank

data class MatchesAddState(
    val maps: List<GameMap>? = null,
    val modes: List<Mode>? = null,
    val ranks: List<Rank>? = null
)
