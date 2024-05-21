package dev.bittim.valolink.feature.auth.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.core.ui.UiText
import dev.bittim.valolink.feature.auth.data.repository.AuthRepository
import dev.bittim.valolink.feature.auth.domain.ValidationUseCases
import dev.bittim.valolink.feature.auth.ui.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val validationUseCases: ValidationUseCases
) : ViewModel() {
    private var _signUpState = MutableStateFlow<SignUpState>(
        SignUpState.Input(
            email = "",
            username = "",
            password = "",
            confirmPassword = "",
            emailError = null,
            usernameError = null,
            passwordError = null,
            confirmPasswordError = null,
            authError = null
        )
    )
    val signUpState = _signUpState.asStateFlow()

    fun onSignUpClicked() = viewModelScope.launch {
        if (_signUpState.value !is SignUpState.Input) return@launch
        val state = _signUpState.value as SignUpState.Input

        val emailError: UiText? = validateEmail(state.email)
        val usernameError: UiText? = validateUsername(state.username)
        val passwordError: UiText? = validatePassword(state.password, state.confirmPassword)

        if (emailError != null || usernameError != null || passwordError != null) {
            _signUpState.update {
                if (it is SignUpState.Input) {
                    it.copy(
                        emailError = emailError,
                        usernameError = usernameError,
                        passwordError = passwordError,
                        confirmPasswordError = passwordError
                    )
                } else {
                    it
                }
            }

            return@launch
        }

        authRepo.signUp(state.email, state.username, state.password).collectLatest { result ->
            when (result) {
                is Result.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }

                is Result.Success -> {
                    _signUpState.value = SignUpState.Success
                }

                is Result.Error -> {
                    _signUpState.value = SignUpState.Input(
                        email = state.email,
                        username = state.username,
                        password = state.password,
                        confirmPassword = "",
                        emailError = UiText.DynamicString(""),
                        usernameError = UiText.DynamicString(""),
                        passwordError = UiText.DynamicString(""),
                        confirmPasswordError = UiText.DynamicString(""),
                        authError = result.error.asUiText()
                    )
                }
            }
        }
    }


    private fun validateEmail(email: String): UiText? {
        return when (val result = validationUseCases.validateEmail(email)) {
            is Result.Error -> result.error.asUiText()

            else -> null
        }
    }

    private fun validateUsername(username: String): UiText? {
        return when (val result = validationUseCases.validateUsername(username)) {
            is Result.Error -> result.error.asUiText()
            else -> null
        }
    }

    private fun validatePassword(password: String, confirmPassword: String? = null): UiText? {
        return when (val result = validationUseCases.validatePassword(password, confirmPassword)) {
            is Result.Error -> result.error.asUiText()
            else -> null
        }
    }
    

    
    fun onEmailChange(email: String) {
        _signUpState.update { state ->
            if (state is SignUpState.Input) {
                state.copy(
                    email = email
                )
            } else {
                state
            }
        }
    }

    fun onUsernameChange(username: String) {
        _signUpState.update { state ->
            if (state is SignUpState.Input) {
                state.copy(
                    username = username
                )
            } else {
                state
            }
        }
    }

    fun onPasswordChange(password: String) {
        _signUpState.update { state ->
            if (state is SignUpState.Input) {
                state.copy(
                    password = password
                )
            } else {
                state
            }
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _signUpState.update { state ->
            if (state is SignUpState.Input) {
                state.copy(
                    confirmPassword = confirmPassword
                )
            } else {
                state
            }
        }
    }
}