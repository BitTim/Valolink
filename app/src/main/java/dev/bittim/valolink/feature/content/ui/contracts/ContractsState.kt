package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import dev.bittim.valolink.feature.content.domain.model.contract.Contract

data class ContractsState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val isLoading: Boolean = true,
    val archiveTypeFilter: ArchiveTypeFilter = ArchiveTypeFilter.SEASON,
    val activeContracts: List<Contract> = listOf(), val agentGears: List<Contract> = listOf(),
    val inactiveContracts: List<Contract> = listOf(),

    val agentGearCarouselState: CarouselState = CarouselState(itemCount = agentGears::count)
)

enum class ArchiveTypeFilter(val displayName: String) {
    SEASON("Season"), EVENT("Event"), RECRUIT("Recruit")
}