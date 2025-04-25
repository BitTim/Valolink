/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   25.04.25, 19:03
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import dev.bittim.valolink.content.domain.model.contract.Contract

data class ContractSetupState(
    val contract: Contract? = null,

    val level: Int = PAGE_OFFSET,
    val xp: Int = 0,
    val freeOnly: Boolean = false
)
