package dev.bittim.valolink.feature.content.ui.contracts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ContractsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ContractsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.getAllContracts().collect { contracts ->
                _state.update { it.copy(isLoading = true) }

                val activeContracts = contracts.filter {
                    it.relation?.startTime != null && it.relation.endTime != null && ZonedDateTime.now()
                        .isAfter(it.relation.startTime) && ZonedDateTime.now()
                        .isBefore(it.relation.endTime)
                }

                val agentContracts = contracts.filter {
                    it.content.relationType == "Agent"
                }

                val inactiveContracts = contracts.filter {
                    (it.relation?.endTime != null && ZonedDateTime.now()
                        .isAfter(it.relation.endTime)) && (it.relation.startTime != null && ZonedDateTime.now()
                        .isAfter(it.relation.startTime))
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        activeContracts = activeContracts,
                        agentContracts = agentContracts,
                        inactiveContracts = inactiveContracts
                    )
                }
            }
        }
    }
}