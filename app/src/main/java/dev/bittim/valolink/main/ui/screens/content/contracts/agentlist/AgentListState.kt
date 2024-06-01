package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import dev.bittim.valolink.main.domain.model.UserData
import dev.bittim.valolink.main.domain.model.game.contract.Contract

data class AgentListState(
    val isLoading: Boolean = true,
    val agentGears: List<Contract> = listOf(),
    val userData: UserData? = null,
)
