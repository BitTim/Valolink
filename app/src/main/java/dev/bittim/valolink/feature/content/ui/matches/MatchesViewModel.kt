package dev.bittim.valolink.feature.content.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private var _matchesState = MutableStateFlow<MatchesState>(MatchesState.Fetching)
    val matchesState = _matchesState.asStateFlow()

    fun onFetch() = viewModelScope.launch {
        _matchesState.value = MatchesState.Content
    }
}