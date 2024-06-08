package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.user.GearRepository
import dev.bittim.valolink.main.data.repository.user.UserRepository
import dev.bittim.valolink.main.domain.usecase.user.AddUserGearUseCase
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
    private val gearRepository: GearRepository,
    private val addUserGearUseCase: AddUserGearUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getCurrentUserData().collectLatest { userData ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userData = userData
                    )
                }
            }
        }

        viewModelScope.launch {
            gearRepository.getCurrentGears().collectLatest { gears ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userGears = gears
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

    fun addUserGear(contract: String) {
        val uid = state.value.userData?.uuid ?: return
        viewModelScope.launch {
            addUserGearUseCase(
                uid,
                contract
            )
        }
    }
}