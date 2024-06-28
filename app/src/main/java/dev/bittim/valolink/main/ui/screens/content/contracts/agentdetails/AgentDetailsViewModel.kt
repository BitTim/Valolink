package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.user.GearRepository
import dev.bittim.valolink.main.data.repository.user.UserRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import dev.bittim.valolink.main.domain.usecase.user.AddUserGearUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val userRepository: UserRepository,
    private val gearRepository: GearRepository,
    private val addUserGearUseCase: AddUserGearUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentDetailsState())
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
    }

    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            contractRepository.getByUuid(uuid, true).collectLatest { contract ->
                _state.update { it.copy(agentGear = contract) }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            currencyRepository.getByUuid(Currency.DOUGH_UUID).collectLatest { currency ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        dough = currency
                    )
                }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            gearRepository.getCurrentGear(uuid).collectLatest { gear ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userGear = gear
                    )
                }
            }
        }
    }

    fun onAbilityChanged(index: Int) {
        _state.update { it.copy(selectedAbility = index) }
    }

    fun unlockAgent() {
        viewModelScope.launch {
            val agent = state.value.agentGear?.content?.relation as Agent? ?: return@launch
            val userData = state.value.userData ?: return@launch

            val newAgents = userData.agents.plus(agent.uuid)
            val newUserData = userData.copy(
                agents = newAgents
            )

            val result = userRepository.setCurrentUserData(newUserData)
            if (!result) Log.e(
                "Valolink",
                "Failed to update userData"
            )
        }
    }

    fun resetAgent() {
        viewModelScope.launch {
            updateProgress(0)

            val agent = state.value.agentGear?.content?.relation as Agent? ?: return@launch
            val userData = state.value.userData ?: return@launch

            if (!agent.isBaseContent) {
                val newAgents = userData.agents.minus(agent.uuid)
                val newUserData = userData.copy(
                    agents = newAgents
                )

                if (newUserData != userData) {
                    userRepository.setCurrentUserData(newUserData)
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

    fun updateProgress(progress: Int) {
        viewModelScope.launch {
            val newGear = state.value.userGear?.copy(progress = progress) ?: return@launch
            gearRepository.setGear(newGear)
        }
    }
}