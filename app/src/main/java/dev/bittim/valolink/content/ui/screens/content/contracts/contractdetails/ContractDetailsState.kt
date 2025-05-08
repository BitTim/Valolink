/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractDetailsState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:08
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.contractdetails

import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.user.domain.model.UserContract

data class ContractDetailsState(
    val isContractLoading: Boolean = false,
    val isCurrencyLoading: Boolean = false,

    val contract: Contract? = null,
    val userContract: UserContract? = null,
    val vp: Currency? = null,
)
