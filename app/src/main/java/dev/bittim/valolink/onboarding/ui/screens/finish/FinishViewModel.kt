/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FinishViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:41
 */

package dev.bittim.valolink.onboarding.ui.screens.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FinishViewModel @Inject constructor(
    private val sprayRepository: SprayRepository,
    private val sessionRepository: SessionRepository,
    private val userMetaRepository: UserMetaRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FinishState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    sprayRepository.getByUuid(FinishScreen.SPRAY_UUID)
                        .onStart { _state.update { it.copy(loading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { spray ->
                            _state.update {
                                it.copy(
                                    loading = false, spray = spray
                                )
                            }
                        }
                }
            }
        }
    }

    fun navBack() {
        viewModelScope.launch {
            val userData = userMetaRepository.getWithCurrentUser().firstOrNull() ?: return@launch
            userMetaRepository.setWithCurrentUser(
                userData.copy(
                    onboardingStep = OnboardingScreen.Finish.step - OnboardingScreen.STEP_OFFSET - 1
                )
            )
        }
    }

    fun finish() {
        viewModelScope.launch {
            val userData = userMetaRepository.getWithCurrentUser().firstOrNull() ?: return@launch
            userMetaRepository.setWithCurrentUser(
                userData.copy(
                    onboardingStep = OnboardingScreen.Finish.step - OnboardingScreen.STEP_OFFSET + 1
                )
            )
        }
    }
}
