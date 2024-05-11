package dev.bittim.valolink.feature.content.ui.contracts.contractdetails

import dev.bittim.valolink.feature.content.domain.model.Currency
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class ContractDetailsState(
    val isLoading: Boolean = true,
    val contract: Contract? = null,
    val vp: Currency? = null,
    val rewards: List<Pair<RewardRelation, ChapterLevel>> = listOf(),
    val freeRewards: List<Pair<RewardRelation, ChapterLevel>> = listOf()
)
