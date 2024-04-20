package dev.bittim.valolink.feature.content.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    init {
        viewModelScope.launch {
            _mainState.update { it.copy(isLoading = true) }

            userRepository.hasUser().collectLatest { isAuthenticated ->
                _mainState.update { it.copy(isLoading = false, isAuthenticated = isAuthenticated) }
            }
        }
    }

    fun onSignOutClicked() {
        userRepository.signOut()
        _mainState.update { it.copy(isAuthenticated = false) }
    }
}