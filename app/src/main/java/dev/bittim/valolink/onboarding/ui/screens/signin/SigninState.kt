package dev.bittim.valolink.onboarding.ui.screens.signin

import dev.bittim.valolink.content.domain.model.Spray
import dev.bittim.valolink.core.ui.util.UiText

data class SigninState(
    val loading: Boolean = false,
    val spray: Spray? = null,

    val emailError: UiText? = null,
)
