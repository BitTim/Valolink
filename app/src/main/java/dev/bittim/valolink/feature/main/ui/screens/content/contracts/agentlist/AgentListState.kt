package dev.bittim.valolink.feature.main.ui.screens.content.contracts.agentlist

import dev.bittim.valolink.feature.main.domain.model.game.contract.Contract

data class AgentListState(
    val isLoading: Boolean = true,
    val agentGears: List<Contract> = listOf(),
)
