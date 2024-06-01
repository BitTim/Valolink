package dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails

import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation

data class ContractDetailsState(
    val isLoading: Boolean = true,
    val contract: Contract? = null,
    val vp: Currency? = null,
    val rewards: List<Pair<RewardRelation, ChapterLevel>> = listOf(),
    val freeRewards: List<Pair<RewardRelation, ChapterLevel>> = listOf(),
)
