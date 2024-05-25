package dev.bittim.valolink.feature.main.ui.screens.content.contracts.agentdetails

import dev.bittim.valolink.feature.main.domain.model.game.Currency
import dev.bittim.valolink.feature.main.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.feature.main.domain.model.game.contract.Contract
import dev.bittim.valolink.feature.main.domain.model.game.contract.RewardRelation

data class AgentDetailsState(
    val isLoading: Boolean = true,
    val agentGear: Contract? = null,
    val selectedAbility: Int = 0,
    val dough: Currency? = null,
    val rewards: List<Pair<RewardRelation, ChapterLevel>> = listOf(),
)
