package dev.bittim.valolink.onboarding.ui.screens.createAccount

import dev.bittim.valolink.content.domain.model.Spray
import dev.bittim.valolink.core.ui.util.UiText

data class CreateAccountState(
    val loading: Boolean = false,
    val spray: Spray? = null,

    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
)
