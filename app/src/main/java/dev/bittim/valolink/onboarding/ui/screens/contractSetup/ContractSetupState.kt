/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 19:10
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import dev.bittim.valolink.content.domain.model.contract.Contract

data class ContractSetupState(
    val isLoading: Boolean = false,

    val contract: Contract? = null,
)
