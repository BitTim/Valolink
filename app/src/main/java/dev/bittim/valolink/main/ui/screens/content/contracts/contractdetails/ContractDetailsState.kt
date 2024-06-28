package dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails

import dev.bittim.valolink.main.domain.model.game.Currency
import dev.bittim.valolink.main.domain.model.game.contract.Contract

data class ContractDetailsState(
    val isLoading: Boolean = true,
    val contract: Contract? = null,
    val vp: Currency? = null,
)
