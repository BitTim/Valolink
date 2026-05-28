/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       PasswordScreenViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.05.26, 20:58
 */

package dev.bittim.valolink.feature.auth.ui.screen.password

import androidx.lifecycle.ViewModel
import dev.bittim.valolink.feature.auth.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.auth_password_error_empty
import valolink.shared.generated.resources.auth_password_error_invalid

class PasswordScreenViewModel(
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PasswordScreenState())
    val state = _state.asStateFlow()

    fun validatePassword(password: String) {
        _state.update {
            it.copy(
                error = when(validatePasswordUseCase(password)) {
                    ValidatePasswordUseCase.PasswordError.EMPTY -> Res.string.auth_password_error_empty
                    ValidatePasswordUseCase.PasswordError.INVALID -> Res.string.auth_password_error_invalid
                    null -> null
                }
            )
        }
    }
}