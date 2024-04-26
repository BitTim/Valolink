package dev.bittim.valolink.feature.content.ui.contracts.agentdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AgentDetailsState())
    val state = _state.asStateFlow()

    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            gameRepository.getContract(uuid).collectLatest { contract ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        agentGear = contract
                    )
                }
            }
        }
    }

    fun onAbilityChanged(index: Int) {
        _state.update { it.copy(selectedAbility = index) }
    }
}