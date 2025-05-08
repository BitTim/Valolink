/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FinishState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:20
 */

package dev.bittim.valolink.onboarding.ui.screens.finish

import dev.bittim.valolink.content.domain.model.Spray

data class FinishState(
    val loading: Boolean = false,
    val spray: Spray? = null,
)
