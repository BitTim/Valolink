/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SigninState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.onboarding.ui.screens.signin

import dev.bittim.valolink.content.domain.model.Spray
import dev.bittim.valolink.core.ui.util.UiText

data class SigninState(
    val loading: Boolean = false,
    val spray: Spray? = null,

    val emailError: UiText? = null,
)
