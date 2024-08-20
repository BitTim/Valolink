package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.user.UserData

data class AgentListState(
    val isUserDataLoading: Boolean = true,
    val isGearsLoading: Boolean = false,

    val agentGears: List<Contract>? = null,
    val userData: UserData? = null,
)
