package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            contractRepository.getLevelByUuid(uuid).flatMapLatest { level ->
                if (level == null) return@flatMapLatest flowOf(null)

                val unlockCurrencyUuid: String
                val price: Int

                if (level.isPurchasableWithDough) {
                    unlockCurrencyUuid = Currency.DOUGH_UUID
                    price = level.doughCost
                } else if (level.isPurchasableWithVP) {
                    unlockCurrencyUuid = Currency.VP_UUID
                    price = level.vpCost
                } else {
                    return@flatMapLatest flowOf(null)
                }

                _state.update {
                    it.copy(
                        level = level,
                        price = price,
                        isGear = level.isPurchasableWithDough
                    )
                }
                currencyRepository.getByUuid(unlockCurrencyUuid)
            }.collectLatest { currency ->
                if (currency == null) return@collectLatest
                _state.update { it.copy(unlockCurrency = currency) }
            }
        }
    }
}