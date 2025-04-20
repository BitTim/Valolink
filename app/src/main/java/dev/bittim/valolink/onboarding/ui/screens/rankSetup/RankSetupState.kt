/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import dev.bittim.valolink.content.domain.model.rank.Rank

data class RankSetupState(
    val ranks: List<Rank>? = null,
)
