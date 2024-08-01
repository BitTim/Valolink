package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.user.data.UserAgentRepository
import dev.bittim.valolink.main.data.repository.user.data.UserContractRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataRepository
import dev.bittim.valolink.main.data.repository.user.data.UserLevelRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.agent.Agent
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
    private val userDataRepository: UserDataRepository,
    private val userAgentRepository: UserAgentRepository,
    private val userContractRepository: UserContractRepository,
    private val userLevelRepository: UserLevelRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentDetailsState())
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
    }

    fun onAbilityChanged(index: Int) {
        _state.update { it.copy(selectedAbility = index) }
    }



    fun unlockAgent() {
        viewModelScope.launch {
            val agent = state.value.agentGear?.content?.relation as Agent? ?: return@launch
            val userData = state.value.userData ?: return@launch

            userAgentRepository.set(agent.toUserObj(userData.uuid))
        }
    }

    fun resetAgent() {
        viewModelScope.launch {
            val contract = state.value.agentGear ?: return@launch
            contract.content.chapters.flatMap { it.levels }.forEach { resetLevel(it.uuid) }

            val agent = state.value.agentGear?.content?.relation as Agent? ?: return@launch
            if (!agent.isBaseContent) {
                val userData = state.value.userData ?: return@launch
                val userAgent = userData.agents.find { it.agent == agent.uuid } ?: return@launch

                userAgentRepository.delete(userAgent)
            }
        }
    }

    fun initUserContract() {
        viewModelScope.launch {
            val contract = state.value.agentGear ?: return@launch
            val userData = state.value.userData ?: return@launch

            userContractRepository.set(contract.toUserObj(userData.uuid))
        }
    }

    fun unlockLevel(uuid: String) {
        viewModelScope.launch {
            val userData = state.value.userData ?: return@launch
            val userContract =
                userData.contracts.find { it.contract == state.value.agentGear?.uuid }
                    ?: return@launch
            val level = state.value.agentGear?.content?.chapters
                ?.flatMap { it.levels }
                ?.find { it.uuid == uuid } ?: return@launch

            userLevelRepository.set(level.toUserObj(userContract.uuid, true))
        }
    }

    fun resetLevel(uuid: String) {
        viewModelScope.launch {
            val userData = state.value.userData ?: return@launch
            val userContract =
                userData.contracts.find { it.contract == state.value.agentGear?.uuid }
                    ?: return@launch
            val userLevel = userContract.levels.find { it.level == uuid } ?: return@launch

            userLevelRepository.delete(userLevel)
        }
    }
}