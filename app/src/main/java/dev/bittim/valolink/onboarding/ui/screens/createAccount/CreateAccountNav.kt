package dev.bittim.valolink.onboarding.ui.screens.createAccount

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CreateAccountNav

fun NavGraphBuilder.createAccountScreen(
    onNavBack: () -> Unit
) {
    composable<CreateAccountNav> {
        val viewModel: CreateAccountViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        CreateAccountScreen(
            state = state.value,
            validateEmail = viewModel::validateEmail,
            validatePassword = viewModel::validatePassword,
            validateConfirmPassword = viewModel::validateConfirmPassword,
            onCancel = onNavBack,
            onCreateAccount = {}
        )
    }
}