/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 01:04
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import dev.bittim.valolink.core.ui.util.UiText

data class ProfileSetupState(
    val imageError: UiText? = null,
    val usernameError: UiText? = null,
)
