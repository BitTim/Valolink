package dev.bittim.valolink.feature.auth.ui.signin

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val SignInNavRoute = "signin"

fun NavGraphBuilder.signInScreen(
    onNavToContent: () -> Unit,
    onNavToSignUp: () -> Unit,
    onNavToForgot: () -> Unit
) {
    composable(SignInNavRoute) {
        val viewModel: SignInViewModel = hiltViewModel()
        val signInState by viewModel.signInState.collectAsStateWithLifecycle()

        SignInScreen(
            state = signInState,
            onEmailValueChange = viewModel::onEmailChange,
            onPasswordValueChange = viewModel::onPasswordChange,
            onSignInClicked = viewModel::onSignInClicked,
            onNavContent = onNavToContent,
            onNavSignUp = onNavToSignUp,
            onNavForgot = onNavToForgot
        )
    }
}

fun NavController.navToSignIn() {
    navigate(SignInNavRoute)
}