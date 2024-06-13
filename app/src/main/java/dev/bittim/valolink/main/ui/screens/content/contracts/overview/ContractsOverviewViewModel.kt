package dev.bittim.valolink.main.ui.screens.content.contracts.overview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.user.GearRepository
import dev.bittim.valolink.main.data.repository.user.UserRepository
import dev.bittim.valolink.main.domain.usecase.user.AddUserGearUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)
@HiltViewModel
class ContractsOverviewViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val userRepository: UserRepository,
    private val gearRepository: GearRepository,
    private val addUserGearUseCase: AddUserGearUseCase,
) : ViewModel() {
    private val _filterState = MutableStateFlow(ArchiveTypeFilter.SEASON)

    private val _state = MutableStateFlow(ContractsOverviewState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getCurrentUserData().collectLatest { userData ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userData = userData
                    )
                }
            }
        }

        viewModelScope.launch {
            gearRepository.getCurrentGears().collectLatest { gears ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userGears = gears
                    )
                }
            }
        }

        viewModelScope.launch {
            contractRepository.getActiveContracts().collectLatest { contracts ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        activeContracts = contracts
                    )
                }
            }
        }

        viewModelScope.launch {
            contractRepository.getAgentGears().collectLatest { gears ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        agentGears = gears,
                        agentGearCarouselState = CarouselState(itemCount = gears::count)
                    )
                }
            }
        }

        viewModelScope.launch {
            _filterState.flatMapLatest {
                contractRepository.getInactiveContracts(it.internalType)
            }.collectLatest { contracts ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        inactiveContracts = contracts
                    )
                }
            }
        }
    }

    fun onArchiveTypeFilterChange(archiveTypeFilter: ArchiveTypeFilter) {
        _state.update { it.copy(archiveTypeFilter = archiveTypeFilter) }
        _filterState.update { archiveTypeFilter }
    }

    fun addUserGear(contract: String) {
        val uid = state.value.userData?.uuid ?: return
        viewModelScope.launch {
            addUserGearUseCase(
                uid,
                contract
            )
        }
    }
}