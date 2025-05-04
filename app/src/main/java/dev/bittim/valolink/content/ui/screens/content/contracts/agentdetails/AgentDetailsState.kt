/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentDetailsState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 12:12
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails

import androidx.compose.foundation.lazy.LazyListState
import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

data class AgentDetailsState(
    val isUserDataLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isCurrencyLoading: Boolean = false,

    val agentGear: Contract? = null,
    val dough: Currency? = null,
    val userAgent: UserAgent? = null,
    val userContract: UserContract? = null,

    val rewardListState: LazyListState = LazyListState(),
)
