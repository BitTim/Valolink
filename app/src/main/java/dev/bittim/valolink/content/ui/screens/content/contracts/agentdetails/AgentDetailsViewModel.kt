/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentDetailsViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 12:25
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyRepository
import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.user.data.repository.data.UserAgentRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserLevelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val userAgentRepository: UserAgentRepository,
    private val userContractRepository: UserContractRepository,
    private val userLevelRepository: UserLevelRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentDetailsState())
    val state = _state.asStateFlow()

    private var fetchUserAgentJob: Job? = null
    private var fetchDetailsJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        fetchDetailsJob?.cancel()
        fetchDetailsJob = viewModelScope.launch {
            launch {
                withContext(Dispatchers.IO) {
                    contractRepository.getByUuid(uuid, true)
                        .onStart { _state.update { it.copy(isContractLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .flatMapLatest { contract ->
                            if (contract != null) {
                                val userContractFlow =
                                    userContractRepository.getWithCurrentUser(contract.uuid)
                                combine(
                                    flowOf(contract),
                                    userContractFlow
                                ) { contract, userContract ->
                                    Pair(contract, userContract)
                                }
                            } else {
                                flowOf(Pair(null, null))
                            }
                        }
                        .collectLatest { contractPair ->
                            _state.update {
                                it.copy(
                                    isContractLoading = false,
                                    agentGear = contractPair.first,
                                    userContract = contractPair.second
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    currencyRepository.getByUuid(Currency.DOUGH_UUID)
                        .onStart { _state.update { it.copy(isCurrencyLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { currency ->
                            _state.update {
                                it.copy(
                                    isCurrencyLoading = false, dough = currency
                                )
                            }
                        }
                }
            }
        }

        fetchUserAgentJob?.cancel()
        fetchUserAgentJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userAgentRepository.getWithCurrentUser(uuid)
                    .onStart { _state.update { it.copy(isUserDataLoading = true) } }
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { userAgent ->
                        _state.update {
                            it.copy(
                                isUserDataLoading = false, userAgent = userAgent
                            )
                        }
                    }
            }
        }
    }


    fun unlockAgent() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val agent = state.value.agentGear?.content?.relation as Agent? ?: return@withContext
                val userData = state.value.userAgent ?: return@withContext

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
                    val userAgent = state.value.userAgent ?: return@withContext
                    userAgentRepository.delete(userAgent)
                }
            }
        }
    }

    fun initUserContract() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract = state.value.agentGear ?: return@withContext
                val userData = state.value.userAgent ?: return@withContext

                userContractRepository.set(contract.toUserObj(userData.uuid, false))
            }
        }
    }

    fun unlockLevel(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userContract = state.value.userContract ?: return@withContext
                val level = state.value.agentGear?.content?.chapters?.flatMap { it.levels }
                    ?.find { it.uuid == uuid } ?: return@withContext

                userLevelRepository.set(level.toUserObj(userContract.uuid, true))
            }
        }
    }

    fun resetLevel(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userContract = state.value.userContract ?: return@withContext
                val userLevel = userContract.levels.find { it.level == uuid } ?: return@withContext

                userLevelRepository.delete(userLevel)
            }
        }
    }
}
