package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails

import androidx.compose.foundation.lazy.LazyListState
import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.user.UserData

data class AgentDetailsState(
    val isUserDataLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isCurrencyLoading: Boolean = false,

    val agentGear: Contract? = null,
    val dough: Currency? = null,
    val userData: UserData? = null,

    val rewardListState: LazyListState = LazyListState(),
)
