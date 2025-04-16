/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CreateAccountViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.onboarding.ui.screens.createAccount

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bittim.valolink.R
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.auth.AuthRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import dev.bittim.valolink.user.domain.error.EmailError
import dev.bittim.valolink.user.domain.error.PasswordError
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
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val sprayRepository: SprayRepository,
    private val sessionRepository: SessionRepository,
    private val userDataRepository: UserDataRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateAccountState())
    val state = _state.asStateFlow()

    private var snackbarHostState: SnackbarHostState? = null
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

    fun validateEmail(email: String): UiText? {
        val emailResult = validateEmailUseCase(email)
        val emailError = when (emailResult) {
            is Result.Ok -> null
            is Result.Err -> {
                when (emailResult.error) {
                    EmailError.EMPTY -> UiText.StringResource(R.string.error_empty)
                    EmailError.INVALID -> UiText.StringResource(R.string.error_email_invalid)
                }
            }
        }

        _state.update { it.copy(emailError = emailError) }
        return emailError
    }

    fun validatePassword(password: String): UiText? {
        val passwordResult = validatePasswordUseCase(password)
        val passwordError = when (passwordResult) {
            is Result.Ok -> null
            is Result.Err -> {
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
        return passwordError
    }

    fun setSnackbarHostState(snackbarHostState: SnackbarHostState) {
        this.snackbarHostState = snackbarHostState
    }

    fun createUser(email: String, password: String) {
        val emailResult = validateEmail(email)
        val passwordResult = validatePassword(password)
        if (emailResult != null || passwordResult != null) return

        viewModelScope.launch {
            val error = authRepository.createAccount(email, password)

            if (error == null) {
                sessionRepository.setLocal(false)
                userDataRepository.createEmptyForCurrentUser()
            } else {
                snackbarHostState?.showSnackbar(error.asString(context))
            }
        }
    }
}
