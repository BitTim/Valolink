package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.user.UserData

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
