package dev.bittim.valolink.feature.content.ui.contracts.agentdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.game.ContractRepository
import dev.bittim.valolink.feature.content.data.repository.game.CurrencyRepository
import dev.bittim.valolink.feature.content.domain.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AgentDetailsState())
    val state = _state.asStateFlow()

    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            contractRepository.getContract(uuid).collectLatest { contract ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        agentGear = contract
                    )
                }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            currencyRepository.getCurrency(Currency.doughUuid).collectLatest { currency ->
                _state.update { it.copy(isLoading = false, dough = currency) }
            }
        }
    }

    fun onAbilityChanged(index: Int) {
        _state.update { it.copy(selectedAbility = index) }
    }
}