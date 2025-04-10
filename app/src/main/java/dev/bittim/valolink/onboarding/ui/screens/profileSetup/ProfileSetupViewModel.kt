/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   11.04.25, 00:25
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.R
import dev.bittim.valolink.core.domain.usecase.profile.GenerateProfilePictureUseCase
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.user.domain.error.UsernameError
import dev.bittim.valolink.user.domain.usecase.validator.ValidateUsernameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val generateProfilePictureUseCase: GenerateProfilePictureUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileSetupState())
    val state = _state.asStateFlow()

    init {
        val avatar = generateProfilePictureUseCase("")
        _state.update { it.copy(avatar = avatar) }
    }

    fun validateUsername(username: String): UiText? {
        val usernameResult = validateUsernameUseCase(username)
        val usernameError = when (usernameResult) {
            is Result.Ok -> null
            is Result.Err -> when (usernameResult.error) {
                UsernameError.EMPTY -> UiText.StringResource(R.string.error_empty)
                UsernameError.TOO_SHORT -> UiText.StringResource(R.string.error_username_tooShort)
            }
        }

        val avatar = generateProfilePictureUseCase(username)
        _state.update { it.copy(usernameError = usernameError, avatar = avatar) }
        return usernameError
    }

    fun setProfile(
        string: String,
        bool: Boolean,
        function: () -> Unit
    ) {
        TODO()
    }
}
