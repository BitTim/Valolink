package dev.bittim.valolink.main.ui.screens.content.contracts.overview

import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import dev.bittim.valolink.user.domain.model.UserData

data class ContractsOverviewState(
    val isUserDataLoading: Boolean = true,
    val isActiveContractsLoading: Boolean = false,
    val isAgentGearsLoading: Boolean = false,
    val isInactiveContractsLoading: Boolean = false,

    val userData: UserData? = null,
    val activeContracts: List<Contract>? = null,
    val agentGears: List<Contract>? = null,
    val inactiveContracts: List<Contract>? = null,

    val archiveTypeFilter: ContentType = ContentType.SEASON,
)