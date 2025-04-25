/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   25.04.25, 19:03
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.domain.model.Season
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractSetupViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ContractSetupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            contractRepository.getActiveContracts(true)
                .map { it.firstOrNull { it.content.relation is Season } }
                .filterNotNull()
                .stateIn(viewModelScope, WhileSubscribed(5000), null).collectLatest {
                    _state.value = _state.value.copy(contract = it)
                }
        }

        viewModelScope.launch {
            val activeContract = contractRepository.getActiveContracts(false)
                .map { it.firstOrNull { it.content.relation is Season } }
                .firstOrNull()
            val userContract =
                userDataRepository.getWithCurrentUser()
                    .map { it?.contracts?.find { it.contract == activeContract?.uuid } }
                    .firstOrNull() ?: return@launch

            _state.update {
                it.copy(
                    level = userContract.levels.count(),
                    xp = userContract.levels.last().xpOffset ?: 0,
                    freeOnly = userContract.freeOnly
                )
            }
        }
    }

    fun navBack() {
        viewModelScope.launch {
            val userData = userDataRepository.getWithCurrentUser().firstOrNull() ?: return@launch
            userDataRepository.setWithCurrentUser(
                userData.copy(
                    onboardingStep = OnboardingScreen.ContractSetup.step - OnboardingScreen.STEP_OFFSET - 1
                )
            )
        }
    }

    fun onLevelChanged(level: Int) {
        _state.update { it.copy(level = level) }
    }

    fun onXpChanged(xp: Int) {
        _state.update { it.copy(xp = xp) }
    }

    fun onFreeOnlyChanged(freeOnly: Boolean) {
        _state.update { it.copy(freeOnly = freeOnly) }
    }

    fun setContractProgress(level: Int, xp: Int, freeOnly: Boolean) {

    }
}
