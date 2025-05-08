/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LandingState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.onboarding.ui.screens.landing

import dev.bittim.valolink.content.domain.model.Spray

data class LandingState(
    val loading: Boolean = false,
    val spray: Spray? = null,
)
