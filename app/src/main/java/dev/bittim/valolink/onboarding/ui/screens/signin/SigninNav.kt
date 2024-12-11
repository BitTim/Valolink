package dev.bittim.valolink.onboarding.ui.screens.signin

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SigninNav

fun NavGraphBuilder.signinScreen(
    onNavBack: () -> Unit
) {
    composable<SigninNav> {
        val viewModel: SigninViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        SigninScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            onForgotPasswordClicked = { },
            onCreateAccountClicked = { },
            onCancelClicked = onNavBack,
            onContinueClicked = { }
        )
    }
}