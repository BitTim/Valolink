/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OtpScreenViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:16
 */

package dev.bittim.valolink.feature.auth.ui.screen.otp

import androidx.lifecycle.ViewModel
import dev.bittim.valolink.core.domain.usecase.SubmitOtpUseCase
import dev.bittim.valolink.feature.auth.domain.repo.AuthFlowRepo
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.auth_email_textField_label

class OtpScreenViewModel (
    private val authFlowRepo: AuthFlowRepo,
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val submitOtpUseCase: SubmitOtpUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OtpScreenState())
    val state = _state.asStateFlow()

    fun onOtpChange(otp: String) {
        _state.update {
            it.copy(
                otp = otp,
                error = when(validateOtpUseCase(otp)) {
                    ValidateOtpUseCase.OtpError.EMPTY -> Res.string.auth_email_textField_label // TODO change with actual string resources
                    ValidateOtpUseCase.OtpError.LENGTH -> Res.string.auth_email_textField_label
                    ValidateOtpUseCase.OtpError.INVALID -> Res.string.auth_email_textField_label
                    else -> null
                }
            )
        }
    }

    fun onComplete() {
        val otp = _state.value.otp

        authFlowRepo.otp = otp
        submitOtpUseCase(authFlowRepo.email, otp)
    }
}