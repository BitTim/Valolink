package dev.bittim.valolink.feature.content.ui.contracts.agentdetails

import dev.bittim.valolink.feature.content.domain.model.Currency
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class AgentDetailsState(
    val isLoading: Boolean = true,
    val agentGear: Contract? = null,
    val selectedAbility: Int = 0,
    val dough: Currency? = null,
    val rewards: List<Pair<RewardRelation, ChapterLevel>> = listOf()
)
