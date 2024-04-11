package dev.bittim.valolink.feature.content.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.data.repository.GameRepository
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
        val versionResult: Result<VersionDto, GameRepository.GameDataError> =
            gameRepository.getApiVersion()

        if (versionResult is Result.Loading) _matchesState.value = MatchesState.Loading
        if (versionResult is Result.Success) {
            _matchesState.value = MatchesState.Content(
                version = versionResult.data,
                error = false
            )
        }
        if (versionResult is Result.Error) {
            _matchesState.value = MatchesState.Content(
                version = null,
                error = true
            )
        }
    }
}