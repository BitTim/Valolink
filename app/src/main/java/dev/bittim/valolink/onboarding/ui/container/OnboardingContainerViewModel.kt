/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.onboarding.ui.container

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.domain.usecase.progress.CalcProgressDecimalUseCase
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.auth.AuthRepository
import dev.bittim.valolink.user.data.repository.synced.UserRepository
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val calcProgressDecimalUseCase: CalcProgressDecimalUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingContainerState())
    val state = _state.asStateFlow()

    val snackbarHostState = MutableStateFlow(SnackbarHostState())

    init {
        viewModelScope.launch {
            authRepository.isAuthenticated()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { isAuthenticated ->
                    _state.update { it.copy(isAuthenticated = isAuthenticated) }
                }
        }

        viewModelScope.launch {
            userRepository.getWithCurrentUser()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { data ->
                    _state.update { it.copy(user = data) }
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
