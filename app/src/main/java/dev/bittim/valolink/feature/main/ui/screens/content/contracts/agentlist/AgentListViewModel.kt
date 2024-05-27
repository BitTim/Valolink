package dev.bittim.valolink.feature.main.ui.screens.content.contracts.agentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.main.data.repository.UserRepository
import dev.bittim.valolink.feature.main.data.repository.game.ContractRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentListViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch { //            userRepository.getUserData().collectLatest { userData ->
            //                _state.update {
            //                    it.copy(
            //                        isLoading = false,
            //                        userData = userData
            //                    )
            //                }
            //            }
        }

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