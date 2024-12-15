/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LevelDetailsState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.user.domain.model.UserData

data class LevelDetailsState(
    val isUserDataLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isLevelLoading: Boolean = false,
    val isLevelRelationsLoading: Boolean = false,

    val userData: UserData? = null,
    val contract: Contract? = null,

    val level: Level? = null,
    val previousLevel: Level? = null,
    val nextLevel: Level? = null,

    val unlockCurrency: Currency? = null,
    val price: Int = 0,

    val isGear: Boolean = false,
)
