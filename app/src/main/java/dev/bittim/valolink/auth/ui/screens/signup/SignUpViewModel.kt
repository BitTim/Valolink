package dev.bittim.valolink.auth.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.auth.data.repository.AuthRepository
import dev.bittim.valolink.auth.domain.ValidationUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val validationUseCases: ValidationUseCases,
) : ViewModel() {
    private var _state = MutableStateFlow(
        SignUpState(
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
    val state = _state.asStateFlow()

    fun onSignUpClicked() = viewModelScope.launch {
        val emailError: String? = validateEmail(state.value.email)
        val usernameError: String? = validateUsername(state.value.username)
        val passwordError: String? = validatePassword(
            state.value.password,
            state.value.confirmPassword
        )

        if (emailError != null || usernameError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    usernameError = usernameError,
                    passwordError = passwordError,
                    confirmPasswordError = passwordError
                )
            }

            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val result = authRepo.signUp(
            state.value.email,
            state.value.username,
            state.value.password
        )

        if (!result) {
            _state.update {
                it.copy(
                    email = state.value.email,
                    username = state.value.username,
                    password = state.value.password,
                    confirmPassword = "",
                    emailError = "",
                    usernameError = "",
                    passwordError = "",
                    confirmPasswordError = "",
                    authError = "Something went wrong",
                    isLoading = false
                )
            }
        } else {
            _state.update {
                it.copy(
                    isSuccess = true,
                    isLoading = false
                )
            }
        }
    }


    private fun validateEmail(email: String): String? {
        return validationUseCases.validateEmail(email)
    }

    private fun validateUsername(username: String): String? {
        return validationUseCases.validateUsername(username)
    }

    private fun validatePassword(
        password: String,
        confirmPassword: String? = null,
    ): String? {
        return validationUseCases.validatePassword(
            password,
            confirmPassword
        )
    }



    fun onEmailChange(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }

    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(username = username)
        }
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update {
            it.copy(confirmPassword = confirmPassword)
        }
    }
}