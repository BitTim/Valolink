/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentListState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 12:28
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentlist

import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

data class AgentListState(
    val isUserDataLoading: Boolean = true,
    val isGearsLoading: Boolean = false,

    val agentGears: List<Contract>? = null,
    val userAgents: List<UserAgent>? = null,
    val userGears: List<UserContract>? = null,
)
