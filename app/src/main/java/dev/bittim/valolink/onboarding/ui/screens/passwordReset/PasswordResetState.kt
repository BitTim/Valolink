/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PasswordResetState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   05.04.25, 11:11
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordReset

import dev.bittim.valolink.content.domain.model.Spray
import dev.bittim.valolink.core.ui.util.UiText

data class PasswordResetState(
    val loading: Boolean = false,
    val spray: Spray? = null,

    val passwordError: UiText? = null
)
