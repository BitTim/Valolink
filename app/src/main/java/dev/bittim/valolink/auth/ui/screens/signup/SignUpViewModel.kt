/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SignUpViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.auth.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.auth.data.repository.AuthRepository
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.user.domain.usecase.validator.EmailError
import dev.bittim.valolink.user.domain.usecase.validator.PasswordError
import dev.bittim.valolink.user.domain.usecase.validator.UsernameError
import dev.bittim.valolink.user.domain.usecase.validator.ValidateEmailUseCase
import dev.bittim.valolink.user.domain.usecase.validator.ValidatePasswordUseCase
import dev.bittim.valolink.user.domain.usecase.validator.ValidateUsernameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateUsernameUseCase: ValidateUsernameUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(
        SignUpState(
            email = "",
            username = "",
            password = "",
            confirmPassword = "",
            emailError = null,
            usernameError = null,
            passwordError = null,
            confirmPasswordError = null,
            authError = null
        )
    )
    val state = _state.asStateFlow()

    fun onSignUpClicked() = viewModelScope.launch {
        val emailResult = validateEmail(state.value.email)
        val usernameResult = validateUsername(state.value.username)
        val passwordResult = validatePassword(state.value.password)

        if (emailResult is Result.Failure || usernameResult is Result.Failure || passwordResult is Result.Failure) {
            _state.update {
                it.copy(
                    emailError = when (emailResult) {
                        is Result.Failure -> {
                            when (emailResult.error) {
                                EmailError.EMPTY -> "Email cannot be empty"
                                EmailError.INVALID -> "Email is invalid"
                            }
                        }

                        is Result.Success -> null
                    },

                    usernameError = when (usernameResult) {
                        is Result.Failure -> {
                            when (usernameResult.error) {
                                UsernameError.EMPTY -> "Username cannot be empty"
                                UsernameError.TOO_SHORT -> "Username must be at least 4 characters long"
                            }
                        }

                        is Result.Success -> null
                    },

                    passwordError = when (passwordResult) {
                        is Result.Failure -> {
                            when (passwordResult.error) {
                                PasswordError.EMPTY -> "Password cannot be empty"
                                PasswordError.TOO_SHORT -> "Password must be at least 12 characters long"
                                PasswordError.NO_DIGIT -> "Password must contain at least one digit"
                                PasswordError.NO_LOWERCASE -> "Password must contain at least one lowercase letter"
                                PasswordError.NO_UPPERCASE -> "Password must contain at least one uppercase letter"
                                PasswordError.NO_SPECIAL_CHAR -> "Password must contain at least one special character"
                            }
                        }

                        is Result.Success -> null
                    }
                )
            }

            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val result = authRepo.signUp(
            state.value.email,
            state.value.username,
            state.value.password
        )

        if (!result) {
            _state.update {
                it.copy(
                    email = state.value.email,
                    username = state.value.username,
                    password = state.value.password,
                    confirmPassword = "",
                    emailError = "",
                    usernameError = "",
                    passwordError = "",
                    confirmPasswordError = "",
                    authError = "Something went wrong",
                    isLoading = false
                )
            }
        } else {
            _state.update {
                it.copy(
                    isSuccess = true,
                    isLoading = false
                )
            }
        }
    }


    private fun validateEmail(email: String): Result<Unit, EmailError> {
        return validateEmailUseCase(email)
    }

    private fun validateUsername(username: String): Result<Unit, UsernameError> {
        return validateUsernameUseCase(username)
    }

    private fun validatePassword(
        password: String
    ): Result<Unit, PasswordError> {
        return validatePasswordUseCase(
            password
        )
    }


    fun onEmailChange(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }

    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(username = username)
        }
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update {
            it.copy(confirmPassword = confirmPassword)
        }
    }
}