/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.onboarding.ui.container

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.domain.usecase.progress.CalcProgressDecimalUseCase
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingContainerViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val calcProgressDecimalUseCase: CalcProgressDecimalUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingContainerState())
    val state = _state.asStateFlow()

    val snackbarHostState = MutableStateFlow(SnackbarHostState())

    init {
        viewModelScope.launch {
            sessionRepository.getAuthenticated().collect {
                _state.value = _state.value.copy(isAuthenticated = it)
            }

            sessionRepository.getOnboardingStep().collect { step ->
                _state.value = _state.value.copy(onboardingStep = step ?: 0)
            }
        }
    }

    fun onDestinationChanged(route: String) {
        val screen = OnboardingScreen.entries.find { route.contains(it.route) }
        _state.value = OnboardingContainerState(
            title = screen?.title,
            description = screen?.description,
            progress = calcProgressDecimalUseCase(screen?.step ?: 0, OnboardingScreen.getMaxStep())
        )
    }
}
