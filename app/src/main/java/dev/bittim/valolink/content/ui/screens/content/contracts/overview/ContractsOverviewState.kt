/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractsOverviewState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:54
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.overview

import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

data class ContractsOverviewState(
    val isUserDataLoading: Boolean = true,
    val isActiveContractsLoading: Boolean = false,
    val isAgentGearsLoading: Boolean = false,
    val isInactiveContractsLoading: Boolean = false,

    val userContracts: List<UserContract>? = null,
    val userAgents: List<UserAgent>? = null,
    val activeContracts: List<Contract>? = null,
    val agentGears: List<Contract>? = null,
    val inactiveContracts: List<Contract>? = null,

    val archiveTypeFilter: ContentType = ContentType.SEASON,
)
