package dev.bittim.valolink.feature.content.ui.contracts

import dev.bittim.valolink.feature.content.domain.model.contract.Contract

sealed interface ContractsState {
    data object Fetching : ContractsState
    data object Loading : ContractsState
    data class Content(
        val contracts: List<Contract>
    ) : ContractsState
}