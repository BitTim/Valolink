/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       EmailScreenViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:09
 */

package dev.bittim.valolink.feature.auth.ui.screen.email

import androidx.lifecycle.ViewModel
import dev.bittim.valolink.core.domain.usecase.SignInWithOtpUseCase
import dev.bittim.valolink.feature.auth.domain.repo.AuthFlowRepo
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateEmailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.auth_email_error_empty
import valolink.shared.generated.resources.auth_email_error_invalid

class EmailScreenViewModel(
    private val authFlowRepo: AuthFlowRepo,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val signInWithOtpUseCase: SignInWithOtpUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EmailScreenState())
    val state = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                error = when(validateEmailUseCase(email)) {
                    ValidateEmailUseCase.EmailError.EMPTY -> Res.string.auth_email_error_empty
                    ValidateEmailUseCase.EmailError.INVALID -> Res.string.auth_email_error_invalid
                    null -> null
                }
            )
        }
    }

    fun onNext(navNext: () -> Unit) {
        val email = _state.value.email

        authFlowRepo.email = email
        signInWithOtpUseCase(email)
        navNext()
    }
}