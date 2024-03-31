package dev.bittim.valolink.feature.content.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    fun onCheckAuth() {
        _homeState.value = HomeState.Loading

        if (userRepository.hasUser()) {
            _homeState.value = HomeState.Content(
                username = userRepository.getUsername() ?: ""
            )
        } else {
            _homeState.value = HomeState.NotAuthorized
        }
    }

    fun onSignOutClicked() {
        userRepository.signOut()
        _homeState.value = HomeState.NotAuthorized
    }
}