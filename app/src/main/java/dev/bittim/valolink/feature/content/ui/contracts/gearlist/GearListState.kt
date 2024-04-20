package dev.bittim.valolink.feature.content.ui.contracts.gearlist

import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class GearListState(
    val isLoading: Boolean = true, val agentGears: List<Contract> = listOf()
)
