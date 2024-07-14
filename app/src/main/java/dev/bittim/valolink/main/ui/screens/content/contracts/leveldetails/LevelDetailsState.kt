package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level

data class LevelDetailsState(
    val isLoading: Boolean = true,
    val level: Level? = null,
    val unlockCurrency: Currency? = null,
)
