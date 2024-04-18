package dev.bittim.valolink.feature.content.ui.contracts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            gameRepository.getAllAgentGears().collect { contracts ->
                _state.update { it.copy(isLoading = true) }

                _state.update {
                    it.copy(
                        isLoading = false, agentGears = contracts
                    )
                }
            }
        }

        viewModelScope.launch {
            gameRepository.getAllInactiveContracts().collect { contracts ->
                _state.update { it.copy(isLoading = true) }

                _state.update {
                    it.copy(
                        isLoading = false, inactiveContracts = contracts
                    )
                }
            }
        }
    }
}