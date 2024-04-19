package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.GameRepository
import dev.bittim.valolink.feature.content.domain.model.Event
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class ContractsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ContractsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.getAllActiveContracts().collect { contracts ->
                _state.update { it.copy(isLoading = true) }

                _state.update {
                    it.copy(
                        isLoading = false, activeContracts = contracts
                    )
                }
            }
        }

        viewModelScope.launch {
            gameRepository.getAllAgentGears().collect { gears ->
                _state.update { it.copy(isLoading = true) }

                _state.update {
                    it.copy(
                        isLoading = false,
                        agentGears = gears,
                        agentGearCarouselState = CarouselState(itemCount = gears::count)
                    )
                }
            }
        }

        viewModelScope.launch {
            gameRepository.getAllInactiveContracts().combine(state) { contracts, state ->
                contracts.filter {
                    when (state.archiveTypeFilter) {
                        ArchiveTypeFilter.SEASON  -> it.content.relation is Season
                        ArchiveTypeFilter.EVENT   -> it.content.relation is Event
                        ArchiveTypeFilter.RECRUIT -> it.content.relation is Agent
                    }
                }
            }.collect { contracts ->
                _state.update { it.copy(isLoading = true) }

                _state.update {
                    it.copy(
                        isLoading = false, inactiveContracts = contracts
                    )
                }
            }
        }
    }

    fun onArchiveTypeFilterChange(archiveTypeFilter: ArchiveTypeFilter) {
        _state.update { it.copy(archiveTypeFilter = archiveTypeFilter) }
    }
}