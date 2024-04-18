package dev.bittim.valolink.feature.content.ui.contracts

import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class ContractsState(
    val isLoading: Boolean = true,
    val activeContracts: List<Contract> = listOf(), val agentGears: List<Contract> = listOf(),
    val inactiveContracts: List<Contract> = listOf()
)