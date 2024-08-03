package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.user.data.UserContractRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataRepository
import dev.bittim.valolink.main.data.repository.user.data.UserLevelRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val userDataRepository: UserDataRepository,
    private val userContractRepository: UserContractRepository,
    private val userLevelRepository: UserLevelRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LevelDetailsState(false))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userDataRepository
                .getWithCurrentUser()
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { userData ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userData = userData
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchDetails(uuid: String?, contract: String?) {
        if (uuid == null || contract == null) return

        // Get passed contract
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            contractRepository
                .getByUuid(contract, true)
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { contract ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            contract = contract
                        )
                    }
                }
        }

        // Get price and unlock currency for level
        viewModelScope.launch {
            contractRepository
                .getLevelByUuid(uuid)
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
                }
                .stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { currency ->
                    if (currency == null) return@collectLatest
                    _state.update { it.copy(unlockCurrency = currency) }
                }
        }
    }



    fun initUserContract() {
        viewModelScope.launch {
            val contract = state.value.contract ?: return@launch
            val userData = state.value.userData ?: return@launch

            userContractRepository.set(contract.toUserObj(userData.uuid))
        }
    }

    fun unlockLevel(uuid: String, isPurchased: Boolean) {
        viewModelScope.launch {
            val userData = state.value.userData ?: return@launch
            val userContract =
                userData.contracts.find { it.contract == state.value.contract?.uuid }
                    ?: return@launch
            val level = state.value.contract?.content?.chapters
                ?.flatMap { it.levels }
                ?.find { it.uuid == uuid } ?: return@launch

            userLevelRepository.set(level.toUserObj(userContract.uuid, isPurchased))
        }
    }

    fun resetLevel(uuid: String) {
        viewModelScope.launch {
            val userData = state.value.userData ?: return@launch
            val userContract =
                userData.contracts.find { it.contract == state.value.contract?.uuid }
                    ?: return@launch
            val userLevel = userContract.levels.find { it.level == uuid } ?: return@launch

            userLevelRepository.delete(userLevel)
        }
    }
}