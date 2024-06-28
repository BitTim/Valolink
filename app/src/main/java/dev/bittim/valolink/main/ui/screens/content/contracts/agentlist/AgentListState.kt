package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.user.Gear
import dev.bittim.valolink.main.domain.model.user.UserData

data class AgentListState(
    val isLoading: Boolean = true,
    val agentGears: List<Contract> = emptyList(),
    val userGears: List<Gear> = emptyList(),
    val userData: UserData? = null,
)
