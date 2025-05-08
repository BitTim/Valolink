/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractDetailsViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:29
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.contractdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyRepository
import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
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
class ContractDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val userContractRepository: UserContractRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ContractDetailsState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            launch {
                withContext(Dispatchers.IO) {
                    contractRepository.getByUuid(uuid, true)
                        .onStart { _state.update { it.copy(isContractLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { contract ->
                            _state.update {
                                it.copy(
                                    contract = contract, isContractLoading = false
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    currencyRepository.getByUuid(Currency.VP_UUID)
                        .onStart { _state.update { it.copy(isCurrencyLoading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { currency ->
                            _state.update {
                                it.copy(
                                    isCurrencyLoading = false, vp = currency
                                )
                            }
                        }
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    userContractRepository.getWithCurrentUser(uuid)
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { userContract ->
                            _state.update { it.copy(userContract = userContract) }
                        }
                }
            }
        }
    }
}
