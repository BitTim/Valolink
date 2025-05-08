/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 23:17
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import dev.bittim.valolink.content.domain.model.rank.Rank

data class RankSetupState(
    val ranks: List<Rank>? = null,

    val tier: Int = 0,
    val rr: Int = 50,
    val matchesPlayed: Int = 0,
    val matchesNeeded: Int = 5,
)
