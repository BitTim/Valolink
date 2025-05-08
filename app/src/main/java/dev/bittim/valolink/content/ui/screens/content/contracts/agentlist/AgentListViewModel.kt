/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentListViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:28
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AgentListViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val sessionRepository: SessionRepository,
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
                    contractRepository.getAgentGears(false)
                        .onStart { _state.update { it.copy(isGearsLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .filterNotNull()
                        .flatMapLatest { gears ->
                            val userGearFlows =
                                gears.map {
                                    userContractRepository.getWithCurrentUser(it.uuid)
                                        .filterNotNull()
                                }
                            val userGearsFlow = combine(userGearFlows) { it.toList() }

                            combine(
                                flowOf(gears),
                                userGearsFlow
                            ) { gears, userGears ->
                                Pair(gears, userGears)
                            }
                        }
                        .collectLatest { gearsPair ->
                            _state.update {
                                it.copy(
                                    isGearsLoading = false,
                                    agentGears = gearsPair.first,
                                    userGears = gearsPair.second
                                )
                            }
                        }
                }
            }
        }
    }

    fun initUserContract(uuid: String) {
        if (state.value.userGears == null) return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract =
                    state.value.agentGears?.find { it.uuid == uuid } ?: return@withContext
                val uid = sessionRepository.getUid().firstOrNull() ?: return@withContext

                userContractRepository.set(contract.toUserObj(uid, freeOnly = false))
            }
        }
    }
}
