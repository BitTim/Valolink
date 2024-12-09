package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AgentListViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val userDataRepository: UserDataRepository,
    private val userContractRepository: UserContractRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentListState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            launch {
                withContext(Dispatchers.IO) {
                    userDataRepository.getWithCurrentUser()
                        .onStart { _state.update { it.copy(isUserDataLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { userData ->
                            _state.update {
                                it.copy(
                                    isUserDataLoading = false, userData = userData
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    contractRepository.getAgentGears()
                        .onStart { _state.update { it.copy(isGearsLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { gears ->
                            _state.update {
                                it.copy(
                                    isGearsLoading = false,
                                    agentGears = gears ?: emptyList(),
                                )
                            }
                        }
                }
            }
        }
    }

    fun initUserContract(uuid: String) {
        if (state.value.userData == null) return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract =
                    state.value.agentGears?.find { it.uuid == uuid } ?: return@withContext
                val userData = state.value.userData ?: return@withContext

                userContractRepository.set(contract.toUserObj(userData.uuid))
            }
        }
    }
}