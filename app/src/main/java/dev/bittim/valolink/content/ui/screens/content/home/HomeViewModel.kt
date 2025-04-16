/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HomeViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.content.ui.screens.content.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDataRepository
                    .getWithCurrentUser()
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { data ->
                        _state.update { it.copy(username = data?.username) }
                    }
            }
        }
    }
}
