package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.user.data.UserContractRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentListViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val userDataRepository: UserDataRepository,
    private val userContractRepository: UserContractRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userDataRepository.getWithCurrentUser().collectLatest { userData ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userData = userData
                    )
                }
            }
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

    fun initUserContract(uuid: String) {
        viewModelScope.launch {
            val contract = state.value.agentGears.find { it.uuid == uuid } ?: return@launch
            val userData = state.value.userData ?: return@launch

            userContractRepository.set(contract.toUserObj(userData.uuid))
        }
    }
}