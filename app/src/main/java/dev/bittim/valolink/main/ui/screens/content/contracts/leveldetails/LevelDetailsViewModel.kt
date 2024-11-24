package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.data.repository.game.ContractRepository
import dev.bittim.valolink.core.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.user.data.UserContractRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataRepository
import dev.bittim.valolink.main.data.repository.user.data.UserLevelRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LevelDetailsViewModel @Inject constructor(
	private val contractRepository: ContractRepository,
	private val currencyRepository: CurrencyRepository,
	private val userDataRepository: UserDataRepository,
	private val userContractRepository: UserContractRepository,
	private val userLevelRepository: UserLevelRepository,
) : ViewModel() {
	private val _state = MutableStateFlow(LevelDetailsState())
	val state = _state.asStateFlow()

	private var fetchUserDataJob: Job? = null
	private var fetchDetailsJob: Job? = null

	init {
		fetchUserDataJob?.cancel()
		fetchUserDataJob = viewModelScope.launch {
			withContext(Dispatchers.IO) {
				userDataRepository.getWithCurrentUser()
					.onStart { _state.update { it.copy(isUserDataLoading = true) } }
					.stateIn(viewModelScope, WhileSubscribed(5000), null)
					.collectLatest { userData ->
						_state.update {
							it.copy(
								isUserDataLoading = false, userData = userData
							)
						}
					}
			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	fun fetchDetails(uuid: String?, contract: String?) {
		if (uuid == null || contract == null) return

		// Get passed contract
		fetchDetailsJob?.cancel()
		fetchDetailsJob = viewModelScope.launch {
			launch {
				withContext(Dispatchers.IO) {
					_state.update { LevelDetailsState() }

					contractRepository.getByUuid(contract, true)
						.onStart { _state.update { it.copy(isContractLoading = true) } }
						.stateIn(viewModelScope, WhileSubscribed(5000), null)
						.collectLatest { contract ->
							_state.update {
								it.copy(
									isContractLoading = false, contract = contract
								)
							}
						}
				}
			}

			// Get price and unlock currency for level
			launch {
				withContext(Dispatchers.IO) {
					contractRepository.getLevelByUuid(uuid)
						.onStart { _state.update { it.copy(isLevelLoading = true) } }
						.flatMapLatest { level ->
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
								unlockCurrencyUuid = ""
								price = -1
							}

							_state.update {
								it.copy(
									isLevelLoading = false,
									level = level,
									price = price,
									isGear = level.isPurchasableWithDough
								)
							}

							// Get data related to level
							val currencyFlow = currencyRepository.getByUuid(unlockCurrencyUuid)
							val prevLevelFlow =
								if (level.dependency != null) contractRepository.getLevelByUuid(
									level.dependency
								) else flowOf(null)
							val nextLevelFlow = contractRepository.getLevelByDependency(level.uuid)

							combine(
								currencyFlow, prevLevelFlow, nextLevelFlow
							) { currency, prevLevel, nextLevel ->
								Pair(currency, Pair(prevLevel, nextLevel))
							}
						}.onStart { _state.update { it.copy(isLevelRelationsLoading = true) } }
						.stateIn(viewModelScope, WhileSubscribed(5000), null)
						.collectLatest { relations ->
							_state.update {
								it.copy(
									isLevelRelationsLoading = false,
									unlockCurrency = relations?.first,
									previousLevel = relations?.second?.first,
									nextLevel = relations?.second?.second
								)
							}
						}
				}
			}
		}
	}



	fun initUserContract() {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				val contract = state.value.contract ?: return@withContext
				val userData = state.value.userData ?: return@withContext

				userContractRepository.set(contract.toUserObj(userData.uuid))
			}
		}
	}

	fun unlockLevel(uuid: String?, isPurchased: Boolean) {
		if (uuid == null) return

		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				val userData = state.value.userData ?: return@withContext
				val userContract =
					userData.contracts.find { it.contract == state.value.contract?.uuid }
						?: return@withContext
				val level = state.value.contract?.content?.chapters?.flatMap { it.levels }
					?.find { it.uuid == uuid } ?: return@withContext

				userLevelRepository.set(level.toUserObj(userContract.uuid, isPurchased))
			}
		}
	}

	fun resetLevel(uuid: String?) {
		if (uuid == null) return

		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				val userData = state.value.userData ?: return@withContext
				val userContract =
					userData.contracts.find { it.contract == state.value.contract?.uuid }
						?: return@withContext
				val userLevel = userContract.levels.find { it.level == uuid } ?: return@withContext

				userLevelRepository.delete(userLevel)
			}
		}
	}
}