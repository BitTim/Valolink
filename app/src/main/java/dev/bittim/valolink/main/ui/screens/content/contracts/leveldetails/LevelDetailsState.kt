package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.domain.model.user.UserContract
import dev.bittim.valolink.main.domain.model.user.UserData

data class LevelDetailsState(
    val isLoading: Boolean = true,
    val userData: UserData? = null,
    val userUserContract: UserContract? = null,

    val level: Level? = null,
    val unlockCurrency: Currency? = null,
    val price: Int = 0,
    val isGear: Boolean = false,
)
