package dev.bittim.valolink.feature.auth.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.util.Resource
import dev.bittim.valolink.feature.auth.data.AuthRepository
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
    private var _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun signUp(email: String, password: String) = viewModelScope.launch {
        authRepo.signUp(email, password).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    _signUpState.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    _signUpState.update { it.copy(success = "Account created successfully") }
                }

                is Resource.Error -> {
                    _signUpState.update { it.copy(error = result.message) }
                }
            }
        }
    }
}