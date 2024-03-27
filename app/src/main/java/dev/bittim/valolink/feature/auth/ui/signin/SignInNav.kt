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
    onNavToSignUp: () -> Unit
) {
    composable(SignInNavRoute) {
        val viewModel: SignInViewModel = hiltViewModel()
        val signInState by viewModel.signInState.collectAsStateWithLifecycle()

        SignInScreen(
            state = signInState,
            snackbarHostState = viewModel.snackbarHostState,
            
            onEmailValueChange = viewModel::onEmailChange,
            onPasswordValueChange = viewModel::onPasswordChange,
            onForgotEmailValueChange = viewModel::onForgotEmailChange,
            onForgotDismissRequest = viewModel::onForgotDismiss,
            onForgotConfirmation = viewModel::onForgotConfirmation,
            onSignInClicked = viewModel::onSignInClicked,
            onForgotClicked = viewModel::onForgotClicked,
            onNavContent = onNavToContent,
            onNavSignUp = onNavToSignUp
        )
    }
}

fun NavController.navToSignIn() {
    navigate(SignInNavRoute) {
        launchSingleTop = true
    }
}