package dev.bittim.valolink.feature.auth.ui.screens.signin

import androidx.compose.material3.SnackbarHostState
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
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val validationUseCases: ValidationUseCases
) : ViewModel() {
    private var _signInState = MutableStateFlow<SignInState>(
        SignInState.Input(
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
    val signInState = _signInState.asStateFlow()
    val snackbarHostState = SnackbarHostState()
    
    fun onSignInClicked() = viewModelScope.launch {
        if (_signInState.value !is SignInState.Input) return@launch
        val state = _signInState.value as SignInState.Input

        val emailError: UiText? = validateEmail(state.email)
        val passwordError: UiText? = validatePassword(state.password)
        
        if (emailError != null || passwordError != null) {
            _signInState.update {
                if (it is SignInState.Input) {
                    it.copy(
                        emailError = emailError,
                        passwordError = passwordError
                    )
                } else {
                    it
                }
            }

            return@launch
        }

        authRepo.signIn(state.email, state.password).collectLatest { result ->
            when (result) {
                is Result.Loading -> {
                    _signInState.value = SignInState.Loading
                }

                is Result.Success -> {
                    _signInState.value = SignInState.Success
                }

                is Result.Error -> {
                    _signInState.value = SignInState.Input(
                        email = state.email,
                        password = state.password,
                        emailError = UiText.DynamicString(""),
                        passwordError = UiText.DynamicString(""),
                        authError = result.error.asUiText(),
                        showForgotDialog = state.showForgotDialog,
                        forgotEmail = state.forgotEmail,
                        forgotEmailError = state.forgotEmailError
                    )
                }
            }
        }
    }

    fun onForgotConfirmation() = viewModelScope.launch {
        if (_signInState.value !is SignInState.Input) return@launch
        val state = _signInState.value as SignInState.Input

        val emailError = validateEmail(state.forgotEmail)

        if (emailError != null) {
            _signInState.update {
                if (it is SignInState.Input) {
                    it.copy(
                        forgotEmailError = emailError
                    )
                } else {
                    it
                }
            }

            return@launch
        }

        authRepo.forgotPassword(state.forgotEmail).collectLatest { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    viewModelScope.launch {
                        snackbarHostState.showSnackbar("Sent password reset request to ${state.forgotEmail}")
                    }
                    onForgotDismiss()
                }

                is Result.Error -> {
                    _signInState.value = SignInState.Input(
                        email = state.email,
                        password = state.password,
                        emailError = state.emailError,
                        passwordError = state.passwordError,
                        authError = state.authError,
                        showForgotDialog = true,
                        forgotEmail = state.forgotEmail,
                        forgotEmailError = result.error.asUiText() // Handle string conversion
                    )
                }
            }
        }
    }


    private fun validateEmail(email: String): UiText? {
        return when (val result = validationUseCases.validateEmail(email)) {
            is Result.Error -> {
                result.error.asUiText()
            }

            else -> null
        }
    }

    private fun validatePassword(password: String): UiText? {
        return when (val result = validationUseCases.validatePassword(password)) {
            is Result.Error -> {
                result.error.asUiText()
            }

            else -> null
        }
    }
    


    fun onForgotClicked() {
        _signInState.update { state ->
            if (state is SignInState.Input) {
                state.copy(
                    showForgotDialog = true
                )
            } else {
                state
            }
        }
    }

    fun onForgotDismiss() {
        _signInState.update { state ->
            if (state is SignInState.Input) {
                state.copy(
                    showForgotDialog = false,
                    forgotEmail = "",
                    forgotEmailError = null
                )
            } else {
                state
            }
        }
    }

    fun onEmailChange(email: String) {
        _signInState.update { state ->
            if (state is SignInState.Input) {
                state.copy(
                    email = email
                )
            } else {
                state
            }
        }
    }

    fun onPasswordChange(password: String) {
        _signInState.update { state ->
            if (state is SignInState.Input) {
                state.copy(
                    password = password
                )
            } else {
                state
            }
        }
    }

    fun onForgotEmailChange(email: String) {
        _signInState.update { state ->
            if (state is SignInState.Input) {
                state.copy(
                    forgotEmail = email
                )
            } else {
                state
            }
        }
    }
}