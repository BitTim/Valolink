/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       GetStartedState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

data class GetStartedState(
    val loadingFinished: Int = 0,
    val userAgents: List<UserAgent> = emptyList(),
    val userContracts: List<UserContract> = emptyList(),
)
