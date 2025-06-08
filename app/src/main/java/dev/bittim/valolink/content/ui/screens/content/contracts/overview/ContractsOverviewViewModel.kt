/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractsOverviewViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.overview

import android.graphics.BitmapFactory
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserAgentRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
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
    private val sessionRepository: SessionRepository,
    private val userMetaRepository: UserMetaRepository,
    private val userAgentRepository: UserAgentRepository,
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
                    contractRepository.getActiveContracts(false)
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
                    contractRepository.getAgentGears(false)
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
                        contractRepository.getInactiveContracts(it, false)
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

            launch {
                withContext(Dispatchers.IO) {
                    val avatarBytes = userMetaRepository.downloadAvatarWithCurrentUser()
                    val avatar = if (avatarBytes != null) BitmapFactory.decodeByteArray(
                        avatarBytes,
                        0,
                        avatarBytes.size
                    ) else null

                    _state.update { it.copy(userAvatar = avatar) }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    userContractRepository.getAllWithCurrentUser()
                        .onStart { _state.update { it.copy(isUserDataLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { contracts ->
                            _state.update {
                                it.copy(
                                    userContracts = contracts,
                                    isUserDataLoading = false,
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    userAgentRepository.getAllWithCurrentUser()
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { agents ->
                            _state.update {
                                it.copy(
                                    userAgents = agents,
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
                val uid = sessionRepository.getUid().firstOrNull() ?: return@withContext

                userContractRepository.set(contract.toUserObj(uid, freeOnly = true))
            }
        }
    }
}
