package dev.bittim.valolink.feature.content.ui.contracts

import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class ContractsState(
    val isLoading: Boolean = true,
    val archiveTypeFilter: ArchiveTypeFilter = ArchiveTypeFilter.SEASON,
    val activeContracts: List<Contract> = listOf(), val agentGears: List<Contract> = listOf(),
    val inactiveContracts: List<Contract> = listOf()
)

enum class ArchiveTypeFilter(val displayName: String) {
    SEASON("Season"), EVENT("Event"), RECRUIT("Recruit")
}