/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SigninViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   05.04.25, 11:06
 */

package dev.bittim.valolink.onboarding.ui.screens.signin

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
import dev.bittim.valolink.user.domain.error.EmailError
import dev.bittim.valolink.user.domain.usecase.validator.ValidateEmailUseCase
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
class SigninViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val sprayRepository: SprayRepository,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SigninState())
    val state = _state.asStateFlow()

    private var snackbarHostState: SnackbarHostState? = null
    private var fetchJob: Job? = null

    init {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            sprayRepository.getByUuid(SigninScreen.SPRAY_UUID)
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

    fun setSnackbarHostState(snackbarHostState: SnackbarHostState) {
        this.snackbarHostState = snackbarHostState
    }

    fun signIn(email: String, password: String, successNav: () -> Unit) {
        val emailResult = validateEmail(email)
        if (emailResult != null) return

        viewModelScope.launch(Dispatchers.IO) {
            val error = authRepository.signIn(email, password)

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
