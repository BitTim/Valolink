package dev.bittim.valolink.feature.auth.ui.util

import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.UiText
import dev.bittim.valolink.feature.auth.data.repository.AuthRepository
import dev.bittim.valolink.feature.auth.domain.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.ValidatePasswordUseCase
import dev.bittim.valolink.feature.auth.domain.ValidateUsernameUseCase

fun ValidateEmailUseCase.EmailError.asUiText(): UiText {
    return when (this) {
        ValidateEmailUseCase.EmailError.EMPTY -> UiText.StringResource(R.string.error_email_empty)
        ValidateEmailUseCase.EmailError.INVALID -> UiText.StringResource(R.string.error_email_invalid)
    }
}

fun ValidateUsernameUseCase.UsernameError.asUiText(): UiText {
    return when (this) {
        ValidateUsernameUseCase.UsernameError.EMPTY -> UiText.StringResource(R.string.error_username_empty)
        ValidateUsernameUseCase.UsernameError.TOO_SHORT -> UiText.StringResource(R.string.error_username_too_short)
    }
}

fun ValidatePasswordUseCase.PasswordError.asUiText(): UiText {
    return when (this) {
        ValidatePasswordUseCase.PasswordError.EMPTY -> UiText.StringResource(R.string.error_password_empty)
        ValidatePasswordUseCase.PasswordError.MISMATCH -> UiText.StringResource(R.string.error_password_mismatch)
        ValidatePasswordUseCase.PasswordError.TOO_SHORT -> UiText.StringResource(R.string.error_password_too_short)
    }
}

fun AuthRepository.AuthError.asUiText(): UiText {
    return when (this) {
        AuthRepository.AuthError.GENERIC -> UiText.StringResource(R.string.error_auth_generic)
    }
}