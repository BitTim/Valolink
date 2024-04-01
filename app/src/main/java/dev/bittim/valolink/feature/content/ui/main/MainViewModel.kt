package dev.bittim.valolink.feature.content.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var _mainState = MutableStateFlow<MainState>(MainState.Loading)
    val mainState = _mainState.asStateFlow()

    fun onCheckAuth() {
        _mainState.value = MainState.Loading

        if (userRepository.hasUser()) {
            _mainState.value = MainState.Content
        } else {
            _mainState.value = MainState.NoAuth
        }
    }

    fun onSignOutClicked() {
        userRepository.signOut()
        _mainState.value = MainState.NoAuth
    }
}