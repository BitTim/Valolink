/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CreateAccountViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.onboarding.ui.screens.createAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.R
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.user.domain.usecase.validator.ConfirmPasswordError
import dev.bittim.valolink.user.domain.usecase.validator.EmailError
import dev.bittim.valolink.user.domain.usecase.validator.PasswordError
import dev.bittim.valolink.user.domain.usecase.validator.ValidateConfirmPasswordUseCase
import dev.bittim.valolink.user.domain.usecase.validator.ValidateEmailUseCase
import dev.bittim.valolink.user.domain.usecase.validator.ValidatePasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val sprayRepository: SprayRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateAccountState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    sprayRepository.getByUuid(CreateAccountScreen.SPRAY_UUID)
                        .onStart { _state.update { it.copy(loading = true) } }
                        .stateIn(viewModelScope, WhileSubscribed(5000), null)
                        .collectLatest { spray ->
                            _state.update {
                                it.copy(
                                    loading = false, spray = spray
                                )
                            }
                        }
                }
            }
        }
    }

    fun validateEmail(email: String) {
        val emailResult = validateEmailUseCase(email)
        val emailError = when (emailResult) {
            is Result.Success -> null
            is Result.Failure -> {
                when (emailResult.error) {
                    EmailError.EMPTY -> UiText.StringResource(R.string.error_empty)
                    EmailError.INVALID -> UiText.StringResource(R.string.error_email_invalid)
                }
            }
        }

        _state.update { it.copy(emailError = emailError) }
    }

    fun validatePassword(password: String) {
        val passwordResult = validatePasswordUseCase(password)
        val passwordError = when (passwordResult) {
            is Result.Success -> null
            is Result.Failure -> {
                when (passwordResult.error) {
                    PasswordError.EMPTY -> UiText.StringResource(R.string.error_empty)
                    PasswordError.NO_DIGIT -> UiText.StringResource(R.string.error_password_noDigit)
                    PasswordError.NO_LOWERCASE -> UiText.StringResource(R.string.error_password_noLowercase)
                    PasswordError.NO_UPPERCASE -> UiText.StringResource(R.string.error_password_noUppercase)
                    PasswordError.NO_SPECIAL_CHAR -> UiText.StringResource(R.string.error_password_noSpecialChar)
                    PasswordError.TOO_SHORT -> UiText.StringResource(
                        R.string.error_password_tooShort,
                        ValidatePasswordUseCase.MIN_PASSWORD_LENGTH
                    )
                }
            }
        }

        _state.update { it.copy(passwordError = passwordError) }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
        val confirmPasswordResult = validateConfirmPasswordUseCase(password, confirmPassword)
        val confirmPasswordError = when (confirmPasswordResult) {
            is Result.Success -> null
            is Result.Failure -> {
                when (confirmPasswordResult.error) {
                    ConfirmPasswordError.EMPTY -> UiText.StringResource(R.string.error_empty)
                    ConfirmPasswordError.NO_MATCH -> UiText.StringResource(R.string.error_confirmPassword_noMatch)
                }
            }
        }

        _state.update { it.copy(confirmPasswordError = confirmPasswordError) }
    }
}