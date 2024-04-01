package dev.bittim.valolink.feature.content.ui.contracts

sealed interface ContractsState {
    data object Fetching : ContractsState
    data object Loading : ContractsState
    data object Content : ContractsState
}