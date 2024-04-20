package dev.bittim.valolink.feature.content.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    fun onFetch() {
        _homeState.update { it.copy(isLoading = true) }

        _homeState.update {
            it.copy(username = userRepository.getUsername())
        } 
    }
}