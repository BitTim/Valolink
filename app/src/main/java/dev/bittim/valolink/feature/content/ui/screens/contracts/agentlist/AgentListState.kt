package dev.bittim.valolink.feature.content.ui.screens.contracts.agentlist

import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class AgentListState(
    val isLoading: Boolean = true, val agentGears: List<Contract> = listOf()
)
