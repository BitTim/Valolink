/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.content.ui.container

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.domain.usecase.QueueFullSyncUseCase
import dev.bittim.valolink.user.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentContainerViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val queueFullSyncUseCase: QueueFullSyncUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(ContentContainerState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            queueFullSyncUseCase()
        }

        viewModelScope.launch {
            sessionRepository.getAuthenticated().collectLatest { isAuthenticated ->
                _state.update { it.copy(isAuthenticated = isAuthenticated) }
            }
        }

        viewModelScope.launch {
            val onboardingStep = sessionRepository.getOnboardingStep().collect { step ->
                _state.update {
                    it.copy(hasOnboarded = step == -1)
                }
            }
        }
    }

    fun onSignOutClicked() {
        viewModelScope.launch {
            sessionRepository.signOut()
            _state.update { it.copy(isAuthenticated = false) }
        }
    }
}
