package dev.bittim.valolink.main.ui.screens.content.contracts.overview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class
)
@HiltViewModel
class ContractsOverviewViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val userDataRepository: UserDataRepository,
    private val userContractRepository: UserContractRepository,
) : ViewModel() {
    private val _filterState = MutableStateFlow(ContentType.SEASON)

    private val _state = MutableStateFlow(ContractsOverviewState())
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
                                    userData = userData,
                                    isUserDataLoading = false,
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    contractRepository.getActiveContracts()
                        .onStart { _state.update { it.copy(isActiveContractsLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), emptyList())
                        .collectLatest { contracts ->
                            _state.update {
                                it.copy(
                                    activeContracts = contracts,
                                    isActiveContractsLoading = false,
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    contractRepository.getAgentGears()
                        .onStart { _state.update { it.copy(isAgentGearsLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), emptyList())
                        .collectLatest { gears ->
                            _state.update {
                                it.copy(
                                    agentGears = gears,
                                    isAgentGearsLoading = false,
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    _filterState.flatMapLatest {
                        contractRepository.getInactiveContracts(it)
                    }.onStart { _state.update { it.copy(isInactiveContractsLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), emptyList())
                        .collectLatest { contracts ->
                            _state.update {
                                it.copy(
                                    inactiveContracts = contracts,
                                    isInactiveContractsLoading = false,
                                )
                            }
                        }
                }
            }
        }
    }

    fun onArchiveTypeFilterChange(contentType: ContentType) {
        _state.update { it.copy(archiveTypeFilter = contentType) }
        _filterState.update { contentType }
    }

    fun initUserContract(uuid: String?) {
        if (uuid == null) return

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