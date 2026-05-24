/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RootScreenViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:58
 */

package dev.bittim.valolink.core.ui.screen.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.domain.usecase.ObserverSessionStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RootScreenViewModel(
    private val observerSessionStatusUseCase: ObserverSessionStatusUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RootScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observerSessionStatusUseCase().collectLatest { authState ->
                _state.update { it.copy(authState = authState) }
            }
        }
    }
}