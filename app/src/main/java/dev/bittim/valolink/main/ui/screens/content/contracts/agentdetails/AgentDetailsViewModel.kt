package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.main.data.repository.game.BuddyRepository
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.game.PlayerCardRepository
import dev.bittim.valolink.main.data.repository.game.PlayerTitleRepository
import dev.bittim.valolink.main.data.repository.game.SprayRepository
import dev.bittim.valolink.main.data.repository.game.WeaponSkinLevelRepository
import dev.bittim.valolink.main.data.repository.user.GearRepository
import dev.bittim.valolink.main.data.repository.user.UserRepository
import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import dev.bittim.valolink.main.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation
import dev.bittim.valolink.main.domain.usecase.user.AddUserGearUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val sprayRepository: SprayRepository,
    private val playerTitleRepository: PlayerTitleRepository,
    private val playerCardRepository: PlayerCardRepository,
    private val buddyRepository: BuddyRepository,
    private val weaponSkinLevelRepository: WeaponSkinLevelRepository,
    private val userRepository: UserRepository,
    private val geaRepository: GearRepository,
    private val addUserGearUseCase: AddUserGearUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AgentDetailsState())
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
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            contractRepository.getContract(uuid).flatMapLatest { contract ->
                _state.update { it.copy(agentGear = contract) }

                val rewardFlows: List<Flow<Pair<RewardRelation, ChapterLevel>>> =
                    contract?.content?.chapters?.flatMap { it.levels }?.map { level ->
                        when (level.reward.rewardType) {
                            "Currency"             -> currencyRepository
                                .getCurrency(level.reward.rewardUuid)
                                .map {
                                    Pair(
                                        it.asRewardRelation(level.reward.amount),
                                        level
                                    )
                                }

                            "Spray"                -> sprayRepository
                                .getSpray(level.reward.rewardUuid)
                                .map {
                                    Pair(
                                        it.asRewardRelation(level.reward.amount),
                                        level
                                    )
                                }

                            "PlayerCard"           -> playerCardRepository
                                .getPlayerCard(level.reward.rewardUuid)
                                .map {
                                    Pair(
                                        it.asRewardRelation(level.reward.amount),
                                        level
                                    )
                                }

                            "Title"                -> playerTitleRepository
                                .getPlayerTitle(level.reward.rewardUuid)
                                .map {
                                    Pair(
                                        it.asRewardRelation(level.reward.amount),
                                        level
                                    )
                                }

                            "EquippableCharmLevel" -> buddyRepository
                                .getByLevelUuid(level.reward.rewardUuid)
                                .map {
                                    Pair(
                                        it.asRewardRelation(level.reward.amount),
                                        level
                                    )
                                }

                            "EquippableSkinLevel"  -> weaponSkinLevelRepository.getWeaponSkinLevel(
                                level.reward.rewardUuid
                            ).map {
                                Pair(
                                    it.asRewardRelation(level.reward.amount),
                                    level
                                )
                            }

                            else                   -> flow { }
                        }
                    } ?: listOf()

                combine(rewardFlows) {
                    it.toList()
                }
            }.collectLatest { rewards ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        rewards = rewards
                    )
                }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            currencyRepository.getCurrency(Currency.DOUGH_UUID).collectLatest { currency ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        dough = currency
                    )
                }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            geaRepository.getCurrentGear(uuid).collectLatest { gear ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        userGear = gear
                    )
                }
            }
        }
    }

    fun onAbilityChanged(index: Int) {
        _state.update { it.copy(selectedAbility = index) }
    }

    fun unlockAgent() {
        viewModelScope.launch {
            val agent = state.value.agentGear?.content?.relation as Agent? ?: return@launch
            val userData = state.value.userData ?: return@launch

            val newAgents = userData.agents.plus(agent.uuid)
            val newUserData = userData.copy(
                agents = newAgents
            )

            val result = userRepository.setCurrentUserData(newUserData)
            if (!result) Log.e(
                "Valolink",
                "Failed to update userData"
            )
        }
    }

    fun resetAgent() {
        viewModelScope.launch {
            updateProgress(0)

            val agent = state.value.agentGear?.content?.relation as Agent? ?: return@launch
            val userData = state.value.userData ?: return@launch

            if (!agent.isBaseContent) {
                val newAgents = userData.agents.minus(agent.uuid)
                val newUserData = userData.copy(
                    agents = newAgents
                )

                if (newUserData != userData) {
                    userRepository.setCurrentUserData(newUserData)
                }
            }
        }
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

    fun updateProgress(progress: Int) {
        viewModelScope.launch {
            val newGear = state.value.userGear?.copy(progress = progress) ?: return@launch
            geaRepository.setGear(newGear)
        }
    }
}