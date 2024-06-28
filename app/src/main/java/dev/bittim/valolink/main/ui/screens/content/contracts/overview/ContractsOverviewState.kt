package dev.bittim.valolink.main.ui.screens.content.contracts.overview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import dev.bittim.valolink.main.domain.model.user.Gear
import dev.bittim.valolink.main.domain.model.user.UserData

data class ContractsOverviewState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val isLoading: Boolean = true,
    val userData: UserData? = null,
    val userGears: List<Gear> = emptyList(),

    val archiveTypeFilter: ContentType = ContentType.SEASON,
    val activeContracts: List<Contract> = emptyList(),
    val agentGears: List<Contract> = emptyList(),
    val inactiveContracts: List<Contract> = emptyList(),

    val agentGearCarouselState: CarouselState = CarouselState(itemCount = agentGears::count),
)