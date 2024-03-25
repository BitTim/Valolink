package dev.bittim.valolink.feature.auth.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.ui.nav.actions.auth.SignInNavActions
import dev.bittim.valolink.core.ui.nav.navigator.Navigator
import dev.bittim.valolink.core.util.Resource
import dev.bittim.valolink.feature.auth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val navigator: Navigator
) : ViewModel() {
    private var _signInState = MutableStateFlow<SignInState>(SignInState.Loading)
    val signInState = _signInState.asStateFlow()

    fun signIn(email: String, password: String) = viewModelScope.launch {
        authRepo.signIn(email, password).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    _signInState.value = SignInState.Loading
                }

                is Resource.Success -> {
                    _signInState.value = SignInState.Success("Sign in successful")
                    navigator.navigate(SignInNavActions.toContent())
                }

                is Resource.Error -> {
                    _signInState.value = SignInState.Error(result.message ?: "An error occurred")
                }
            }
        }
    }

    fun navToSignUp() {
        navigator.navigate(SignInNavActions.toSignUp())
    }

    fun navToForgot() {
        navigator.navigate(SignInNavActions.toForgot())
    }
}