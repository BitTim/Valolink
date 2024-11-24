package dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.data.repository.game.ContractRepository
import dev.bittim.valolink.core.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContractDetailsViewModel @Inject constructor(
	private val contractRepository: ContractRepository,
	private val currencyRepository: CurrencyRepository,
) : ViewModel() {
	private val _state = MutableStateFlow(ContractDetailsState())
	val state = _state.asStateFlow()

	private var fetchJob: Job? = null

	fun fetchDetails(uuid: String?) {
		if (uuid == null) return

		fetchJob?.cancel()
		fetchJob = viewModelScope.launch {
			launch {
				withContext(Dispatchers.IO) {
					contractRepository.getByUuid(uuid, true)
						.onStart { _state.update { it.copy(isContractLoading = true) } }
						.stateIn(viewModelScope, WhileSubscribed(5000), null)
						.collectLatest { contract ->
							_state.update {
								it.copy(
									contract = contract, isContractLoading = false
								)
							}
						}
				}
			}

			launch {
				withContext(Dispatchers.IO) {
					currencyRepository.getByUuid(Currency.VP_UUID)
						.onStart { _state.update { it.copy(isCurrencyLoading = true) } }
						.stateIn(viewModelScope, WhileSubscribed(5000), null)
						.collectLatest { currency ->
							_state.update {
								it.copy(
									isCurrencyLoading = false, vp = currency
								)
							}
						}
				}
			}
		}
	}
}