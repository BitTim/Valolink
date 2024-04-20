package dev.bittim.valolink.feature.auth.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.feature.auth.ui.signin.SignInNavRoute
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
            onNavToSignUp = { navController.navToSignUp() }
        )

        signUpScreen(
            onNavToOnboardingGraph = onNavToOnboardingGraph,
            onNavToSignIn = { navController.popBackStack() }
        )
    }
}

fun NavController.navToAuthGraph() {
    navigate(AuthNavRoute) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}