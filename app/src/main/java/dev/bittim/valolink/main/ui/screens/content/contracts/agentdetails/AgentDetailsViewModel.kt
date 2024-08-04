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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var fetchUserDataJob: Job? = null
    private var fetchDetailsJob: Job? = null

    init {
        fetchUserDataJob?.cancel()
        fetchUserDataJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDataRepository
                    .getWithCurrentUser()
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { userData ->
                        _state.update { it.copy(userData = userData) }
                    }
            }
        }
    }

    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        fetchDetailsJob?.cancel()
        fetchDetailsJob = viewModelScope.launch {
            launch {
                withContext(Dispatchers.IO) {
                    contractRepository
                        .getByUuid(uuid, true)
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { contract ->
                            _state.update { it.copy(agentGear = contract) }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    currencyRepository
                        .getByUuid(Currency.DOUGH_UUID)
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { currency ->
                            _state.update { it.copy(dough = currency) }
                        }
                }
            }
        }
    }

    fun onAbilityChanged(index: Int) {
        _state.update { it.copy(selectedAbility = index) }
    }



    fun unlockAgent() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val agent = state.value.agentGear?.content?.relation as Agent? ?: return@withContext
                val userData = state.value.userData ?: return@withContext

                userAgentRepository.set(agent.toUserObj(userData.uuid))
            }
        }
    }

    fun resetAgent() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract = state.value.agentGear ?: return@withContext
                contract.content.chapters.flatMap { it.levels }.forEach { resetLevel(it.uuid) }

                val agent = state.value.agentGear?.content?.relation as Agent? ?: return@withContext
                if (!agent.isBaseContent) {
                    val userData = state.value.userData ?: return@withContext
                    val userAgent = userData.agents.find { it.agent == agent.uuid }
                        ?: return@withContext

                    userAgentRepository.delete(userAgent)
                }
            }
        }
    }

    fun initUserContract() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract = state.value.agentGear ?: return@withContext
                val userData = state.value.userData ?: return@withContext

                userContractRepository.set(contract.toUserObj(userData.uuid))
            }
        }
    }

    fun unlockLevel(uuid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userData = state.value.userData ?: return@withContext
                val userContract =
                    userData.contracts.find { it.contract == state.value.agentGear?.uuid }
                        ?: return@withContext
                val level = state.value.agentGear?.content?.chapters
                    ?.flatMap { it.levels }
                    ?.find { it.uuid == uuid } ?: return@withContext

                userLevelRepository.set(level.toUserObj(userContract.uuid, true))
            }
        }
    }

    fun resetLevel(uuid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userData = state.value.userData ?: return@withContext
                val userContract =
                    userData.contracts.find { it.contract == state.value.agentGear?.uuid }
                        ?: return@withContext
                val userLevel = userContract.levels.find { it.level == uuid } ?: return@withContext

                userLevelRepository.delete(userLevel)
            }
        }
    }
}