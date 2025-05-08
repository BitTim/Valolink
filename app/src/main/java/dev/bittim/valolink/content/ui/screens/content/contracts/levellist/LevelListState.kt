/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelListState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:47
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.levellist

import androidx.compose.foundation.lazy.LazyListState
import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

data class LevelListState(
    val isUserAgentsLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isCurrencyLoading: Boolean = false,

    val userAgents: List<UserAgent>? = null,
    val userContract: UserContract? = null,
    val contract: Contract? = null,
    val currency: Currency? = null,

    val rewardListState: LazyListState = LazyListState(),
)
