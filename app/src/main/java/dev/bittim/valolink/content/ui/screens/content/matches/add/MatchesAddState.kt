/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesAddState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 02:20
 */

package dev.bittim.valolink.content.ui.screens.content.matches.add

import dev.bittim.valolink.content.domain.model.map.GameMap
import dev.bittim.valolink.content.domain.model.mode.Mode
import dev.bittim.valolink.content.domain.model.rank.Rank
import dev.bittim.valolink.user.domain.model.UserRank

data class MatchesAddState(
    val maps: List<GameMap>? = null,
    val modes: List<Mode>? = null,
    val ranks: List<Rank>? = null,

    val userRank: UserRank? = null,
)
