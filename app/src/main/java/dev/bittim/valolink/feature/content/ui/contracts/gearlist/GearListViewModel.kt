package dev.bittim.valolink.feature.content.ui.contracts.gearlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.ContractRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GearListViewModel @Inject constructor(
    private val contractRepository: ContractRepository
) : ViewModel() {
    private val _state = MutableStateFlow(GearListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            contractRepository.getAgentGears().collectLatest { gears ->
                _state.update { it.copy(isLoading = true) }

                _state.update {
                    it.copy(
                        isLoading = false,
                        agentGears = gears,
                    )
                }
            }
        }
    }
}