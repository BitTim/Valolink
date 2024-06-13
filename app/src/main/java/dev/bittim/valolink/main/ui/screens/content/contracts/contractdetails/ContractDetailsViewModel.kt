package dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails

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
import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation
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
class ContractDetailsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val sprayRepository: SprayRepository,
    private val playerTitleRepository: PlayerTitleRepository,
    private val playerCardRepository: PlayerCardRepository,
    private val buddyRepository: BuddyRepository,
    private val weaponSkinLevelRepository: WeaponSkinLevelRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ContractDetailsState())
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchDetails(uuid: String?) {
        if (uuid == null) return

        viewModelScope.launch {
            contractRepository.getContract(uuid).flatMapLatest { contract ->
                _state.update { it.copy(contract = contract) }

                val rewardFlows: List<Flow<Pair<RewardRelation, ChapterLevel>>> =
                    contract?.content?.chapters?.flatMap { it.levels }?.map { level ->
                        getRewardAsFlow(
                            level.reward.rewardType,
                            level.reward.rewardUuid,
                            level
                        )
                    } ?: listOf()

                val freeRewardFlows: List<Flow<Pair<RewardRelation, ChapterLevel>>> =
                    contract?.content?.chapters?.flatMap { chapter ->
                        chapter.freeRewards?.map { reward ->
                            getRewardAsFlow(
                                reward.rewardType,
                                reward.rewardUuid,
                                chapter.levels.last()
                            )
                        } ?: listOf()
                    } ?: listOf()

                val rewardsFlow = combine(rewardFlows) {
                    it.toList()
                }

                val freeRewardsFlow = combine(freeRewardFlows) {
                    it.toList()
                }

                rewardsFlow.combine(freeRewardsFlow) { rewards, freeRewards ->
                    Pair(
                        rewards,
                        freeRewards
                    )
                }
            }.collectLatest { rewards ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        rewards = rewards.first,
                        freeRewards = rewards.second
                    )
                }
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            currencyRepository.getCurrency(Currency.VP_UUID).collectLatest { currency ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        vp = currency
                    )
                }
            }
        }
    }

    private suspend fun getRewardAsFlow(
        type: String,
        uuid: String,
        level: ChapterLevel,
    ): Flow<Pair<RewardRelation, ChapterLevel>> {
        return when (type) {
            "Currency"             -> currencyRepository.getCurrency(uuid).map {
                Pair(
                    it.asRewardRelation(level.reward.amount),
                    level
                )
            }

            "Spray"                -> sprayRepository.getSpray(uuid).map {
                Pair(
                    it.asRewardRelation(level.reward.amount),
                    level
                )
            }

            "PlayerCard"           -> playerCardRepository.getPlayerCard(uuid).map {
                Pair(
                    it.asRewardRelation(level.reward.amount),
                    level
                )
            }

            "Title"                -> playerTitleRepository.getPlayerTitle(uuid).map {
                Pair(
                    it.asRewardRelation(level.reward.amount),
                    level
                )
            }

            "EquippableCharmLevel" -> buddyRepository.getByLevelUuid(uuid).map {
                Pair(
                    it.asRewardRelation(level.reward.amount),
                    level
                )
            }

            "EquippableSkinLevel"  -> weaponSkinLevelRepository.getWeaponSkinLevel(
                uuid
            ).map {
                Pair(
                    it.asRewardRelation(level.reward.amount),
                    level
                )
            }

            else                   -> flow { }
        }
    }
}