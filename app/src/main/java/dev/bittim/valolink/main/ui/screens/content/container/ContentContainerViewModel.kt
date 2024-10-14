package dev.bittim.valolink.main.ui.screens.content.container

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.user.SessionRepository
import dev.bittim.valolink.main.domain.usecase.game.QueueFullSyncUseCase
import io.github.jan.supabase.auth.status.SessionStatus
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
            sessionRepository.getSessionStatus().collectLatest { sessionStatus ->
                when (sessionStatus) {
                    is SessionStatus.Authenticated    -> {
                        _state.update {
                            it.copy(isAuthenticated = true)
                        }
                    }

                    is SessionStatus.NotAuthenticated -> {
                        _state.update {
                            it.copy(isAuthenticated = false)
                        }
                    }

                    else                              -> {}
                }
            }
        }

        viewModelScope.launch {
            val hasOnboarded = sessionRepository.getHasOnboarded()
            _state.update {
                it.copy(
                    hasOnboarded = hasOnboarded,
                )
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