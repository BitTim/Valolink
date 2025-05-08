/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelListViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.levellist

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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LevelListViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val userContractRepository: UserContractRepository,
    private val userAgentRepository: UserAgentRepository,
    private val userLevelRepository: UserLevelRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LevelListState())
    val state = _state.asStateFlow()

    private var userAgentsFetchJob: Job? = null
    private var contractFetchJob: Job? = null

    init {
        userAgentsFetchJob?.cancel()
        userAgentsFetchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userAgentRepository.getAllWithCurrentUser()
                    .onStart { _state.update { it.copy(isUserAgentsLoading = true) } }
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { userAgents ->
                        _state.update {
                            it.copy(
                                isUserAgentsLoading = false, userAgents = userAgents
                            )
                        }
                    }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        contractFetchJob?.cancel()
        contractFetchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                contractRepository.getByUuid(uuid, true)
                    .onStart { _state.update { it.copy(isContractLoading = true) } }
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .filterNotNull()
                    .flatMapLatest { contract ->
                        val userContractFlow =
                            userContractRepository.getWithCurrentUser(contract.uuid)
                        combine(flowOf(contract), userContractFlow) { contract, userContract ->
                            Pair(contract, userContract)
                        }
                    }
                    .flatMapLatest { contractPair ->
                        val contract = contractPair.first

                        _state.update {
                            it.copy(
                                isContractLoading = false,
                                contract = contract,
                                userContract = contractPair.second
                            )
                        }

                        val firstLevel =
                            contract.content.chapters.firstOrNull()?.levels?.firstOrNull()
                        val currencyUuid =
                            if (firstLevel?.isPurchasableWithDough == true) Currency.DOUGH_UUID
                            else if (firstLevel?.isPurchasableWithVP == true) Currency.VP_UUID
                            else ""

                        currencyRepository.getByUuid(currencyUuid)
                            .onStart { _state.update { it.copy(isCurrencyLoading = true) } }
                            .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    }.collectLatest { currency ->
                        _state.update {
                            it.copy(
                                currency = currency, isCurrencyLoading = false
                            )
                        }
                    }
            }
        }
    }

    fun initUserContract() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract = state.value.contract ?: return@withContext
                val userData = state.value.userContract ?: return@withContext

                userContractRepository.set(contract.toUserObj(userData.uuid, freeOnly = true))
            }
        }
    }

    fun resetContract() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contract = state.value.contract ?: return@withContext
                contract.content.chapters.flatMap { it.levels }.forEach { resetLevel(it.uuid) }

                val relation = state.value.contract?.content?.relation
                if (relation is Agent && !relation.isBaseContent) {
                    val userAgent =
                        state.value.userAgents?.find { it.agent == relation.uuid }
                            ?: return@withContext

                    userAgentRepository.delete(userAgent)
                } else {
                    val userContract = state.value.userContract ?: return@withContext
                    userContractRepository.delete(userContract)
                }
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
