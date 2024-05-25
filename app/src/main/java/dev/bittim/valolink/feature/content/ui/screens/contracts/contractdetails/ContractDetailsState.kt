package dev.bittim.valolink.feature.content.ui.screens.contracts.contractdetails

import dev.bittim.valolink.feature.content.domain.model.game.Currency
import dev.bittim.valolink.feature.content.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.game.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.game.contract.RewardRelation

data class ContractDetailsState(
    val isLoading: Boolean = true,
    val contract: Contract? = null,
    val vp: Currency? = null,
    val rewards: List<Pair<RewardRelation, ChapterLevel>> = listOf(),
    val freeRewards: List<Pair<RewardRelation, ChapterLevel>> = listOf()
)
