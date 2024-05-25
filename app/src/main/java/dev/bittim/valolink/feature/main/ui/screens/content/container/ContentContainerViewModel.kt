package dev.bittim.valolink.feature.main.ui.screens.content.container

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.main.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentContainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private var _state = MutableStateFlow(ContentContainerState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val hasUser = userRepository.hasUser()
            _state.update {
                it.copy(
                    isAuthenticated = hasUser
                )
            }

            if (!hasUser) return@launch

            val userPrefs = userRepository.getUserPrefs()
            val hasOnboarded = userPrefs?.data?.get("hasOnboarded") as Boolean? ?: false

            _state.update {
                it.copy(
                    hasOnboarded = hasOnboarded,
                )
            }
        }
    }

    fun onSignOutClicked() {
        viewModelScope.launch {
            userRepository.signOut()
            _state.update { it.copy(isAuthenticated = false) }
        }
    }



    companion object {
        const val CHECK_AUTH_INTERVAL: Long = 10000
    }
}