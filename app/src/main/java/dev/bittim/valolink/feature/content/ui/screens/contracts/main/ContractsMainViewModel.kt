package dev.bittim.valolink.feature.content.ui.screens.contracts.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.ContractRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class ContractsMainViewModel @Inject constructor(
    private val contractRepository: ContractRepository
) : ViewModel() {
    private val _filterState = MutableStateFlow(ArchiveTypeFilter.SEASON)
    
    private val _state = MutableStateFlow(ContractsMainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            contractRepository.getActiveContracts().collectLatest { contracts ->
                _state.update {
                    it.copy(
                        isLoading = false, activeContracts = contracts
                    )
                }
            }
        }

        viewModelScope.launch {
            contractRepository.getAgentGears().collectLatest { gears ->
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
            _filterState.flatMapLatest {
                contractRepository.getInactiveContracts(it.internalType)
            }.collectLatest { contracts ->
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
        _filterState.update { archiveTypeFilter }
    }
}