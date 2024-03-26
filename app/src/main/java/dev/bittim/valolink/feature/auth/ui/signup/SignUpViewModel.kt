package dev.bittim.valolink.feature.auth.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.util.Resource
import dev.bittim.valolink.feature.auth.data.AuthRepository
import dev.bittim.valolink.feature.auth.util.AuthValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository
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

        val emailError = AuthValidator.validateEmail(state.email)
        val usernameError = AuthValidator.validateUsername(state.username)
        val passwordError = AuthValidator.validatePassword(state.password)
        val confirmPasswordError =
            AuthValidator.validateConfirmPassword(state.password, state.confirmPassword)

        if (emailError != null || usernameError != null || passwordError != null || confirmPasswordError != null) {
            _signUpState.update {
                if (it is SignUpState.Input) {
                    it.copy(
                        emailError = emailError,
                        usernameError = usernameError,
                        passwordError = passwordError,
                        confirmPasswordError = confirmPasswordError
                    )
                } else {
                    it
                }
            }

            return@launch
        }

        authRepo.signUp(state.email, state.username, state.password).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }

                is Resource.Success -> {
                    _signUpState.value = SignUpState.Success
                }

                is Resource.Error -> {
                    _signUpState.value = SignUpState.Input(
                        email = state.email,
                        username = state.username,
                        password = state.password,
                        confirmPassword = "",
                        emailError = "",
                        usernameError = "",
                        passwordError = "",
                        confirmPasswordError = "",
                        authError = result.message
                    )
                }
            }
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