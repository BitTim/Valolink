/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.06.26, 19:31
 */

package dev.bittim.valolink.feature.auth.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.domain.repo.AuthRepo
import dev.bittim.valolink.core.domain.usecase.SignInWithOtpUseCase
import dev.bittim.valolink.core.domain.usecase.SubmitOtpUseCase
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateOtpUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import valolink.shared.generated.resources.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

class AuthFlowViewModel(
    private val authRepo: AuthRepo,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val signInWithOtpUseCase: SignInWithOtpUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val submitOtpUseCase: SubmitOtpUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AuthFlowState())
    val state = _state.asStateFlow()

    private val heroIconRotationStep = 180f
    private var timerJob: Job? = null
    private var cooldownEndTime: Instant? = null
    private var lastSentEmail: String? = null

    private fun internalNavBack(): Boolean {
        val oldIndex = _state.value.step.index
        val newIndex = oldIndex.minus(1).coerceIn(0, AuthFlowStep.entries.lastIndex)
        if (newIndex == oldIndex) return false

        _state.update {
            it.copy(
                step = AuthFlowStep.entries[newIndex],
                heroIconRotation = it.heroIconRotation - heroIconRotationStep
            )
        }

        return true
    }

    private fun monitorCooldown() {
        val endTime = cooldownEndTime ?: return

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val now = Clock.System.now()
                val remaining = endTime - now
                val remainingSeconds = remaining.inWholeSeconds.toInt()

                if(remainingSeconds <= 0) {
                    _state.update { it.copy(cooldownSecondsLeft = 0) }
                    cooldownEndTime = null
                    break
                }

                _state.update { it.copy(cooldownSecondsLeft = remainingSeconds) }
                delay(500L.milliseconds)
            }
        }
    }

    private fun sendEmail() {
        val email = _state.value.email

        signInWithOtpUseCase(email)
        lastSentEmail = email
        cooldownEndTime = Clock.System.now().plus(authRepo.emailCooldown)
        monitorCooldown()
    }

    fun onAction(action: AuthFlowAction) {
        when (action) {
            is AuthFlowAction.Back -> if(!internalNavBack()) action.navBack()
            is AuthFlowAction.EmailChange -> {
                _state.update {
                    it.copy(
                        email = action.email,
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
                if (email != lastSentEmail) sendEmail()

                _state.update {
                    it.copy(
                        step = AuthFlowStep.OtpStep,
                        heroIconRotation = heroIconRotationStep,
                    )
                }
            }
            is AuthFlowAction.OtpChange -> {
                _state.update {
                    it.copy(
                        otp = action.otp,
                        otpError = when(validateOtpUseCase(action.otp)) {
                            ValidateOtpUseCase.OtpError.EMPTY -> Res.string.auth_otp_error_empty
                            ValidateOtpUseCase.OtpError.LENGTH -> Res.string.auth_otp_error_length
                            ValidateOtpUseCase.OtpError.INVALID -> Res.string.auth_otp_error_invalid
                            else -> null
                        }
                    )
                }
            }
            is AuthFlowAction.OtpResend -> {
                val email = _state.value.email
                val cooldownSecondsLeft = _state.value.cooldownSecondsLeft

                if (email == lastSentEmail && cooldownSecondsLeft == 0) sendEmail()
            }
            is AuthFlowAction.OtpContinue -> {
                val email = _state.value.email
                val otp = _state.value.otp
                submitOtpUseCase(email, otp)
            }
        }
    }
}