/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.onboarding.ui.container

import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.user.domain.model.UserMeta

data class OnboardingContainerState(
    val isAuthenticated: Boolean? = null,
    val userMeta: UserMeta? = null,

    val route: String? = null,
    val title: UiText? = null,
    val description: UiText? = null,
    val progress: Float? = null,
)
