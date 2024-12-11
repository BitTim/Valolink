package dev.bittim.valolink.onboarding.ui.screens.signin

import dev.bittim.valolink.content.domain.model.Spray
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.domain.usecase.validator.EmailError
import dev.bittim.valolink.user.domain.usecase.validator.PasswordError

data class SigninState(
    val loading: Boolean = false,
    val spray: Spray? = null,

    val emailResult: Result<Unit, EmailError>? = null,
    val authResult: Result<Unit, PasswordError>? = null,
)
