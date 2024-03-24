package dev.bittim.valolink.navgraph

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.bittim.valolink.presentation.auth.signin.SignInScreen
import dev.bittim.valolink.presentation.auth.signin.SignInState
import dev.bittim.valolink.presentation.auth.signin.SignInViewModel
import dev.bittim.valolink.screen.AuthScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = NavGraph.AUTH.route,
        startDestination = AuthScreen.SIGNIN.route
    ) {
        composable(AuthScreen.SIGNIN.route) {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        lifecycleScope.launch {
                            viewModel.signInWithIntent(result.data)
                        }
                    }
                }
            )
            
            SignInScreen(
                SignInState(),
                onSignInClick = {
                    navController.popBackStack()
                    navController.navigate(NavGraph.CONTENT.route)
                }
//                onSignUpClick = {
//                    navController.navigate(AuthScreen.SIGNUP.route)
//                },
//                onForgotPasswordClick = {
//                    navController.navigate(AuthScreen.FORGOT_PASSWORD.route)
//                }
            )
        }
        composable(AuthScreen.SIGNUP.route) {
            // SignUpScreen()
        }
        composable(AuthScreen.FORGOT_PASSWORD.route) {
            // ForgotPasswordScreen()
        }
    }
}