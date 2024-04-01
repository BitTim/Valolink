package dev.bittim.valolink.feature.content.ui.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    
) : ViewModel() {
    private var _friendsState = MutableStateFlow<FriendsState>(FriendsState.Fetching)
    val friendsState = _friendsState.asStateFlow()

    fun onFetch() {
        _friendsState.value = FriendsState.Content
    }
}