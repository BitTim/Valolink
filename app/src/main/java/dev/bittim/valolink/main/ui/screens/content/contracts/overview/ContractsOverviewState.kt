package dev.bittim.valolink.main.ui.screens.content.contracts.overview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import dev.bittim.valolink.main.domain.model.UserData
import dev.bittim.valolink.main.domain.model.game.contract.Contract

data class ContractsOverviewState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val isLoading: Boolean = true,
    val userData: UserData? = null,

    val archiveTypeFilter: ArchiveTypeFilter = ArchiveTypeFilter.SEASON,
    val activeContracts: List<Contract> = listOf(),
    val agentGears: List<Contract> = listOf(),
    val inactiveContracts: List<Contract> = listOf(),

    val agentGearCarouselState: CarouselState = CarouselState(itemCount = agentGears::count),
)

enum class ArchiveTypeFilter(
    val displayName: String,
    val internalType: String,
) {
    SEASON(
        "Season",
        "Season"
    ),
    EVENT(
        "Event",
        "Event"
    ),
    RECRUIT(
        "Recruit",
        "Recruitment"
    )
}