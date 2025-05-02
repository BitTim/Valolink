/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:03
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.domain.model.Season
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import dev.bittim.valolink.user.data.repository.data.UserLevelRepository
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
    private val sessionRepository: SessionRepository,
    private val userDataRepository: UserDataRepository,
    private val userContractRepository: UserContractRepository,
    private val userLevelRepository: UserLevelRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ContractSetupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            contractRepository.getActiveContracts(true)
                .map { it.firstOrNull { it.content.relation is Season } }
                .filterNotNull()
                .stateIn(viewModelScope, WhileSubscribed(5000), null).collectLatest { contract ->
                    _state.update { it.copy(contract = contract) }
                }
        }

        viewModelScope.launch {
            val activeContract = contractRepository.getActiveContracts(false)
                .map { it.firstOrNull { it.content.relation is Season } }
                .firstOrNull() ?: return@launch
            val userContract =
                userContractRepository.getWithCurrentUser(activeContract.uuid).firstOrNull()
                    ?: return@launch

            _state.update {
                it.copy(
                    level = userContract.levels.count(),
                    xp = userContract.levels.lastOrNull()?.xpOffset ?: 0,
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
        viewModelScope.launch {
            val uid = sessionRepository.getUid().firstOrNull() ?: return@launch
            val contractUuid = state.value.contract?.uuid ?: return@launch

            val oldUserContract = userContractRepository.get(uid, contractUuid).firstOrNull()
            if (oldUserContract != null) userContractRepository.delete(oldUserContract)

            val userContract = state.value.contract?.toUserObj(uid, freeOnly) ?: return@launch
            userContractRepository.set(userContract)

            val levels = state.value.contract?.content?.chapters?.flatMap { it.levels }
                ?.take(level) ?: emptyList()
            for (i in levels.indices) {
                userLevelRepository.set(
                    levels[i].toUserObj(
                        userContract.uuid,
                        false,
                        if (i == levels.lastIndex) xp else levels[i].xp
                    )
                )
            }

            val userData = userDataRepository.get(uid).firstOrNull() ?: return@launch
            userDataRepository.set(
                uid,
                userData.copy(
                    onboardingStep = OnboardingScreen.ContractSetup.step - OnboardingScreen.STEP_OFFSET + 1
                )
            )
        }
    }
}
