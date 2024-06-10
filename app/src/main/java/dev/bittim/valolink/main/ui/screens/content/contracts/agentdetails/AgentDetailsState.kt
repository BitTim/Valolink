package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails

import androidx.compose.foundation.lazy.LazyListState
import dev.bittim.valolink.main.domain.model.Gear
import dev.bittim.valolink.main.domain.model.UserData
import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation

data class AgentDetailsState(
    val isLoading: Boolean = true,
    val agentGear: Contract? = null,
    val userGear: Gear? = null,
    val selectedAbility: Int = 0,
    val dough: Currency? = null,
    val rewards: List<Pair<RewardRelation, ChapterLevel>> = listOf(),
    val userData: UserData? = null,

    val rewardListState: LazyListState = LazyListState(),
)
