package dev.bittim.valolink.feature.auth.ui.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Stateful
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.signInState.collectAsStateWithLifecycle().value
    SignInScreen(
        state,
        viewModel::signIn,
        viewModel::navToSignUp,
        viewModel::navToForgot
    )
}

// Stateless
@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClicked: (String, String) -> Unit,
    onSignUpClicked: () -> Unit,
    onForgotClicked: () -> Unit
) {
    Column {
        when (state) {
            is SignInState.Loading -> {
                CircularProgressIndicator()
            }

            is SignInState.Success -> {
                Text(text = state.success)
            }

            is SignInState.Error -> {
                Text(text = state.error)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        state = SignInState.Loading,
        onSignInClicked = { _, _ -> },
        onSignUpClicked = {},
        onForgotClicked = {}
    )
}