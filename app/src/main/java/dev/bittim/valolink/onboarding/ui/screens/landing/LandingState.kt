package dev.bittim.valolink.onboarding.ui.screens.landing

import dev.bittim.valolink.content.domain.model.Spray

data class LandingState(
    val loading: Boolean = false,
    val spray: Spray? = null,
)
