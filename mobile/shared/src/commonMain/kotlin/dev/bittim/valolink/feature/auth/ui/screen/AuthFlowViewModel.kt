/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 21:40
 */

package dev.bittim.valolink.feature.auth.ui.screen

import androidx.lifecycle.ViewModel
import dev.bittim.valolink.core.domain.usecase.SignInWithOtpUseCase
import dev.bittim.valolink.core.domain.usecase.SubmitOtpUseCase
import dev.bittim.valolink.core.ui.components.SpinDirection
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import valolink.shared.generated.resources.*

class AuthFlowViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val signInWithOtpUseCase: SignInWithOtpUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val submitOtpUseCase: SubmitOtpUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AuthFlowState())
    val state = _state.asStateFlow()

    private fun internalNav(steps: Int): Boolean {
        val oldIndex = _state.value.step.index
        val newIndex = oldIndex.plus(steps).coerceIn(0, AuthFlowStep.entries.lastIndex)
        if (newIndex == oldIndex) return false

        val spinDirection = when {
            newIndex < oldIndex -> SpinDirection.CounterClockwise
            newIndex > oldIndex -> SpinDirection.Clockwise
            else -> SpinDirection.None
        }

        _state.value = _state.value.copy(
            step = AuthFlowStep.entries[newIndex],
            heroIconSpinDirection = spinDirection
        )

        return true
    }

    fun onAction(action: AuthFlowAction) {
        when (action) {
            is AuthFlowAction.Back -> if(!internalNav(-1)) action.navBack()
            is AuthFlowAction.EmailChange -> {
                _state.update {
                    it.copy(
                        email = action.email,
                        heroIconSpinDirection = SpinDirection.None,
                        emailError = when(validateEmailUseCase(action.email)) {
                            ValidateEmailUseCase.EmailError.EMPTY -> Res.string.auth_landing_error_empty
                            ValidateEmailUseCase.EmailError.INVALID -> Res.string.auth_landing_error_invalid
                            null -> null
                        }
                    )
                }
            }
            is AuthFlowAction.EmailContinue -> {
                val email = _state.value.email
                signInWithOtpUseCase(email)
                _state.update {
                    it.copy(
                        step = AuthFlowStep.OtpStep,
                        heroIconSpinDirection = SpinDirection.Clockwise
                    )
                }
            }
            is AuthFlowAction.OtpChange -> {
                _state.update {
                    it.copy(
                        otp = action.otp,
                        heroIconSpinDirection = SpinDirection.None,
                        otpError = when(validateOtpUseCase(action.otp)) {
                            ValidateOtpUseCase.OtpError.EMPTY -> Res.string.auth_otp_error_empty
                            ValidateOtpUseCase.OtpError.LENGTH -> Res.string.auth_otp_error_length
                            ValidateOtpUseCase.OtpError.INVALID -> Res.string.auth_otp_error_invalid
                            else -> null
                        }
                    )
                }
            }
            is AuthFlowAction.OtpContinue -> {
                val email = _state.value.email
                val otp = _state.value.otp
                submitOtpUseCase(email, otp)
            }
        }
    }
}