package dev.bittim.valolink.feature.content.ui.contracts.agentdetails

import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class AgentDetailsState(
    val isLoading: Boolean = true,
    val agentGear: Contract? = null
)
