package dev.bittim.valolink.feature.content.ui.screens.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
) : ViewModel() {
    private var _matchesState = MutableStateFlow(MatchesState())
    val matchesState = _matchesState.asStateFlow()

    fun onFetch() = viewModelScope.launch {
        _matchesState.update { it.copy(isLoading = true) }
    }
}