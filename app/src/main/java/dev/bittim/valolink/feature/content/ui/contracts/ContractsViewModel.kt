package dev.bittim.valolink.feature.content.ui.contracts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private var _contractsState = MutableStateFlow<ContractsState>(ContractsState.Fetching)
    val contractsState = _contractsState.asStateFlow()

    fun onFetch() = viewModelScope.launch {
        _contractsState.value = ContractsState.Loading

        gameRepository.getAllSeasons().collectLatest { seasons ->
            _contractsState.value = ContractsState.Content(
                seasons = seasons
            )
        }
    }
}