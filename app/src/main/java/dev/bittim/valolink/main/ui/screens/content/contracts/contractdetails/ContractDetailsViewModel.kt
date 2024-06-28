package dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ContractDetailsState())
    val state = _state.asStateFlow()

    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            contractRepository.getByUuid(uuid, true).collectLatest { contract ->
                _state.update { it.copy(contract = contract) }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            currencyRepository.getByUuid(Currency.VP_UUID).collectLatest { currency ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        vp = currency
                    )
                }
            }
        }
    }
}