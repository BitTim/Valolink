package dev.bittim.valolink.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.bittim.valolink.screen.AuthScreen
import dev.bittim.valolink.screen.auth.SignInScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = NavGraph.AUTH.route,
        startDestination = AuthScreen.SIGNIN.route
    ) {
        composable(AuthScreen.SIGNIN.route) {
            SignInScreen(
                onSignInClick = {
                    navController.popBackStack()
                    navController.navigate(NavGraph.CONTENT.route)
                },
                onSignUpClick = {
                    navController.navigate(AuthScreen.SIGNUP.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(AuthScreen.FORGOT_PASSWORD.route)
                }
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