package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.chapter.ChapterLevel

data class LevelDetailsState(
    val isLoading: Boolean = true,
    val level: ChapterLevel? = null,
    val unlockCurrency: Currency? = null,
)
