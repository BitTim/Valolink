package dev.bittim.valolink.feature.content.ui.matches

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    
) : ViewModel() {
    private var _matchesState = MutableStateFlow<MatchesState>(MatchesState.Fetching)
    val matchesState = _matchesState.asStateFlow()

    fun onFetch() {
        _matchesState.value = MatchesState.Content
    }
}