package dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails

import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract

data class ContractDetailsState(
    val isContractLoading: Boolean = false,
    val isCurrencyLoading: Boolean = false,

    val contract: Contract? = null,
    val vp: Currency? = null,
)
