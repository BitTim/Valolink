/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.container

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.domain.usecase.QueueFullSyncUseCase
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import kotlinx.coroutines.Dispatchers
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
class ContentContainerViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userMetaRepository: UserMetaRepository,
    private val queueFullSyncUseCase: QueueFullSyncUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(ContentContainerState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            queueFullSyncUseCase()
        }

        viewModelScope.launch {
            sessionRepository.isAuthenticated()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { isAuthenticated ->
                    _state.update { it.copy(isAuthenticated = isAuthenticated) }
                }
        }

        viewModelScope.launch {
            userMetaRepository.hasOnboardedWithCurrentUser()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { hasOnboarded ->
                    _state.update {
                        it.copy(hasOnboarded = hasOnboarded)
                    }
                }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val avatarBytes = userMetaRepository.downloadAvatarWithCurrentUser()
                val avatar = if (avatarBytes != null) BitmapFactory.decodeByteArray(
                    avatarBytes,
                    0,
                    avatarBytes.size
                ) else null

                _state.update { it.copy(userAvatar = avatar) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            sessionRepository.signOut()
        }
    }
}
