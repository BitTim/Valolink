package dev.bittim.valolink.auth.ui.screens.signin

import androidx.compose.material3.SnackbarHostState
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
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val validationUseCases: ValidationUseCases,
) : ViewModel() {
    private var _state = MutableStateFlow(
        SignInState(
            email = "",
            password = "",
            emailError = null,
            passwordError = null,
            authError = null,
            showForgotDialog = false,
            forgotEmail = "",
            forgotEmailError = null
        )
    )
    val state = _state.asStateFlow()
    val snackbarHostState = SnackbarHostState()

    fun onSignInClicked() = viewModelScope.launch {
        val emailError: String? = validateEmail(state.value.email)
        val passwordError: String? = validatePassword(state.value.password)

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }

            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val result = authRepo.signIn(
            state.value.email,
            state.value.password
        )

        if (!result) {
            _state.update {
                it.copy(
                    email = state.value.email,
                    password = state.value.password,
                    emailError = "",
                    passwordError = "",
                    authError = "Something went wrong",
                    showForgotDialog = state.value.showForgotDialog,
                    forgotEmail = state.value.forgotEmail,
                    forgotEmailError = state.value.forgotEmailError,
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

    fun onForgotConfirmation() = viewModelScope.launch {
        val emailError = validateEmail(state.value.forgotEmail)

        if (emailError != null) {
            _state.update {
                it.copy(forgotEmailError = emailError)
            }

            return@launch
        }

        val result = true // TODO: Implement forgot password mechanisms later

        if (result) {
            viewModelScope.launch {
                snackbarHostState.showSnackbar("Sent password reset request to ${state.value.forgotEmail}")
            }
            onForgotDismiss()
        } else {
            _state.update {
                it.copy(
                    email = state.value.email,
                    password = state.value.password,
                    emailError = state.value.emailError,
                    passwordError = state.value.passwordError,
                    authError = state.value.authError,
                    showForgotDialog = true,
                    forgotEmail = state.value.forgotEmail,
                    forgotEmailError = "Something went wrong"
                )
            }
        }
    }


    private fun validateEmail(email: String): String? {
        return validationUseCases.validateEmail(email)
    }

    private fun validatePassword(password: String): String? {
        return validationUseCases.validatePassword(password)
    }



    fun onForgotClicked() {
        _state.update {
            it.copy(showForgotDialog = true)
        }
    }

    fun onForgotDismiss() {
        _state.update {
            it.copy(
                showForgotDialog = false,
                forgotEmail = "",
                forgotEmailError = null
            )
        }
    }

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

    fun onForgotEmailChange(email: String) {
        _state.update {
            it.copy(forgotEmail = email)
        }
    }
}