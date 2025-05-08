/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PasswordResetViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   05.04.25, 11:12
 */

package dev.bittim.valolink.onboarding.ui.screens.passwordReset

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
import dev.bittim.valolink.user.data.repository.auth.AuthRepository
import dev.bittim.valolink.user.domain.error.PasswordError
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
class PasswordResetViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val sprayRepository: SprayRepository,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PasswordResetState())
    val state = _state.asStateFlow()

    private var snackbarHostState: SnackbarHostState? = null
    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    sprayRepository.getByUuid(PasswordResetScreen.SPRAY_UUID)
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

    fun validatePassword(password: String) {
        val passwordResult = validatePasswordUseCase(password)
        val passwordError = when (passwordResult) {
            is Result.Ok -> null
            is Result.Err -> {
                when (passwordResult.error) {
                    PasswordError.EMPTY -> UiText.StringResource(R.string.error_empty)
                    PasswordError.TOO_SHORT -> UiText.StringResource(R.string.error_password_tooShort)
                    PasswordError.NO_DIGIT -> UiText.StringResource(R.string.error_password_noDigit)
                    PasswordError.NO_LOWERCASE -> UiText.StringResource(R.string.error_password_noLowercase)
                    PasswordError.NO_UPPERCASE -> UiText.StringResource(R.string.error_password_noUppercase)
                    PasswordError.NO_SPECIAL_CHAR -> UiText.StringResource(R.string.error_password_noSpecialChar)
                }
            }
        }

        _state.update { it.copy(passwordError = passwordError) }
    }

    fun setSnackbarHostState(snackbarHostState: SnackbarHostState) {
        this.snackbarHostState = snackbarHostState
    }

    fun resetPassword(password: String, successNav: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = authRepository.resetPassword(password)

            withContext(Dispatchers.Main) {
                if (error == null) {
                    successNav()
                } else {
                    snackbarHostState?.showSnackbar(error.asString(context))
                }
            }
        }
    }
}
