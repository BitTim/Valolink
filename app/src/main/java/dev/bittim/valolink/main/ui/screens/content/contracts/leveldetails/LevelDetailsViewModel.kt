package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LevelDetailsState(false))
    val state = _state.asStateFlow()

    fun fetchDetails(uuid: String?, unlockCurrencyUuid: String) {
        if (uuid == null) return

        viewModelScope.launch {
            contractRepository.getLevelByUuid(uuid).collectLatest { level ->
                _state.update { it.copy(level = level) }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            currencyRepository.getByUuid(unlockCurrencyUuid).collectLatest { currency ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        unlockCurrency = currency
                    )
                }
            }
        }
    }
}