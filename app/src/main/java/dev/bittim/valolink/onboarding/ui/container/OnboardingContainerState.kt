/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.onboarding.ui.container

import dev.bittim.valolink.core.ui.util.UiText

data class OnboardingContainerState(
    val isAuthenticated: Boolean = false,
    val onboardingStep: Int = 0,

    val title: UiText? = null,
    val description: UiText? = null,
    val progress: Float? = null
)
