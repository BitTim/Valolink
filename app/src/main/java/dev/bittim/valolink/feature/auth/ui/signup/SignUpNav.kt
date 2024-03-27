package dev.bittim.valolink.feature.auth.ui.signup

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val SignUpNavRoute = "signup"

fun NavGraphBuilder.signUpScreen(
    onNavToOnboardingGraph: () -> Unit,
    onNavToSignIn: () -> Unit
) {
    composable(SignUpNavRoute) {
        val viewModel: SignUpViewModel = hiltViewModel()
        val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()

        SignUpScreen(
            signUpState,
            onEmailValueChange = viewModel::onEmailChange,
            onUsernameValueChange = viewModel::onUsernameChange,
            onPasswordValueChange = viewModel::onPasswordChange,
            onConfirmPasswordValueChange = viewModel::onConfirmPasswordChange,
            onSignUpClicked = viewModel::onSignUpClicked,
            onNavToOnboardingGraph = onNavToOnboardingGraph,
            onNavToSignIn = onNavToSignIn,
        )
    }
}

fun NavController.navToSignUp() {
    navigate(SignUpNavRoute) {
        launchSingleTop = true
    }
}