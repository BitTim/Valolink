package dev.bittim.valolink.feature.content.ui.contracts

import dev.bittim.valolink.feature.content.domain.model.Season

sealed interface ContractsState {
    data object Fetching : ContractsState
    data object Loading : ContractsState
    data class Content(
        val seasons: List<Season>
    ) : ContractsState
}