package dev.bittim.valolink.feature.content.ui.screens.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    
) : ViewModel() {
    private var _friendsState = MutableStateFlow(FriendsState())
    val friendsState = _friendsState.asStateFlow()

    fun onFetch() {
        _friendsState.update { it.copy(isLoading = true) }
    }
}