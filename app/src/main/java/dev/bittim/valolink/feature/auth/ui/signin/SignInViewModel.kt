package dev.bittim.valolink.feature.auth.ui.signin

import androidx.compose.material3.SnackbarHostState
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
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepository
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

        val emailError = AuthValidator.validateEmail(state.email)
        val passwordError = AuthValidator.validatePassword(state.password)

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
                is Resource.Loading -> {
                    _signInState.value = SignInState.Loading
                }

                is Resource.Success -> {
                    _signInState.value = SignInState.Success
                }

                is Resource.Error -> {
                    _signInState.value = SignInState.Input(
                        email = state.email,
                        password = state.password,
                        emailError = "",
                        passwordError = "",
                        authError = result.message,
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

        val emailError = AuthValidator.validateEmail(state.forgotEmail)

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
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModelScope.launch {
                        snackbarHostState.showSnackbar("Sent password reset request to ${state.forgotEmail}")
                    }
                    onForgotDismiss()
                }

                is Resource.Error -> {
                    _signInState.value = SignInState.Input(
                        email = state.email,
                        password = state.password,
                        emailError = state.emailError,
                        passwordError = state.passwordError,
                        authError = state.authError,
                        showForgotDialog = true,
                        forgotEmail = state.forgotEmail,
                        forgotEmailError = result.message
                    )
                }
            }
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