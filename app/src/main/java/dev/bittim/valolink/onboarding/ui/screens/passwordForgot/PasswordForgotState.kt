/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PasswordForgotState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.12.24, 17:27
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordForgot

import dev.bittim.valolink.content.domain.model.Spray
import dev.bittim.valolink.core.ui.util.UiText

data class PasswordForgotState(
    val loading: Boolean = false,
    val spray: Spray? = null,

    val emailError: UiText? = null
)
