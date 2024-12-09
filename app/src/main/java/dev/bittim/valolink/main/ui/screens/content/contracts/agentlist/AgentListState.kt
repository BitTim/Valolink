package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.user.domain.model.UserData

data class AgentListState(
    val isUserDataLoading: Boolean = true,
    val isGearsLoading: Boolean = false,

    val agentGears: List<Contract>? = null,
    val userData: UserData? = null,
)
