package dev.bittim.valolink.feature.auth.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.feature.auth.ui.signin.SignInNavRoute
import dev.bittim.valolink.feature.auth.ui.signin.navToSignIn
import dev.bittim.valolink.feature.auth.ui.signin.signInScreen
import dev.bittim.valolink.feature.auth.ui.signup.navToSignUp
import dev.bittim.valolink.feature.auth.ui.signup.signUpScreen

const val AuthNavRoute = "auth"
fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onNavToContentGraph: () -> Unit,
    onNavToOnboardingGraph: () -> Unit
) {
    navigation(
        startDestination = SignInNavRoute,
        route = AuthNavRoute
    ) {
        signInScreen(
            onNavToContent = onNavToContentGraph,
            onNavToSignUp = { navController.navToSignUp() },
            onNavToForgot = {}
        )

        signUpScreen(
            onNavToOnboardingGraph = onNavToOnboardingGraph,
            onNavToSignIn = { navController.navToSignIn() }
        )
    }
}

fun NavController.navToAuthGraph() {
    navigate(AuthNavRoute) {
        popUpTo(AuthNavRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}