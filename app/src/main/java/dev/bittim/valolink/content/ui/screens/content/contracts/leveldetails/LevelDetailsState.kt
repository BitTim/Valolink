/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelDetailsState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:41
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.user.domain.model.UserContract

data class LevelDetailsState(
    val isUserDataLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isLevelLoading: Boolean = false,
    val isLevelRelationsLoading: Boolean = false,

    val userContract: UserContract? = null,
    val contract: Contract? = null,

    val level: Level? = null,
    val previousLevel: Level? = null,
    val nextLevel: Level? = null,

    val unlockCurrency: Currency? = null,
    val price: Int = 0,

    val isGear: Boolean = false,
)
