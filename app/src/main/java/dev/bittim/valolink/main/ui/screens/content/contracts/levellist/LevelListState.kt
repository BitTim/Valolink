package dev.bittim.valolink.main.ui.screens.content.contracts.levellist

import androidx.compose.foundation.lazy.LazyListState
import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.user.domain.model.UserData

data class LevelListState(
    val isUserDataLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isCurrencyLoading: Boolean = false,

    val userData: UserData? = null,
    val contract: Contract? = null,
    val currency: Currency? = null,

    val rewardListState: LazyListState = LazyListState(),
)
