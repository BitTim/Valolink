/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingContainerState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.12.24, 21:01
 */

package dev.bittim.valolink.onboarding.ui.container

import dev.bittim.valolink.core.ui.util.UiText

data class OnboardingContainerState(
    val title: UiText? = null,
    val description: UiText? = null,
    val progress: Float? = null
)
