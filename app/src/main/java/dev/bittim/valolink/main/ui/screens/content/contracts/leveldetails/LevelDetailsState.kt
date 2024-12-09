package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.user.domain.model.UserData

data class LevelDetailsState(
    val isUserDataLoading: Boolean = true,
    val isContractLoading: Boolean = false,
    val isLevelLoading: Boolean = false,
    val isLevelRelationsLoading: Boolean = false,

    val userData: UserData? = null,
    val contract: Contract? = null,

    val level: Level? = null,
    val previousLevel: Level? = null,
    val nextLevel: Level? = null,

    val unlockCurrency: Currency? = null,
    val price: Int = 0,

    val isGear: Boolean = false,
)
