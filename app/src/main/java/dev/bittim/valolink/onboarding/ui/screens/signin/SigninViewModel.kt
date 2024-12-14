/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SigninViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.onboarding.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.R
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.user.domain.usecase.validator.EmailError
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
    private val sprayRepository: SprayRepository,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SigninState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                withContext(Dispatchers.IO) {
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
}