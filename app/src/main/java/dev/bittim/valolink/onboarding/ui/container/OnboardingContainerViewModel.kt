/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.onboarding.ui.container

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.domain.usecase.progress.CalcProgressDecimalUseCase
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingContainerViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userMetaRepository: UserMetaRepository,
    private val calcProgressDecimalUseCase: CalcProgressDecimalUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingContainerState())
    val state = _state.asStateFlow()

    val snackbarHostState = MutableStateFlow(SnackbarHostState())

    init {
        viewModelScope.launch {
            sessionRepository.isAuthenticated()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { isAuthenticated ->
                    _state.update { it.copy(isAuthenticated = isAuthenticated) }
                }
        }

        viewModelScope.launch {
            userMetaRepository.getWithCurrentUser()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { data ->
                    _state.update { it.copy(userMeta = data) }
                }
        }
    }

    fun onDestinationChanged(route: String) {
        val screen = OnboardingScreen.entries.find { route.contains(it.route) }
        _state.update {
            it.copy(
                title = screen?.title,
                route = route,
                description = screen?.description,
                progress = calcProgressDecimalUseCase(
                    screen?.step ?: 0,
                    OnboardingScreen.getMaxStep()
                )
            )
        }
    }
}
