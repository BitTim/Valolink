package dev.bittim.valolink.feature.auth.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object AuthNavGraph

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onNavToContentGraph: () -> Unit,
    onNavToOnboardingGraph: () -> Unit
) {
    navigation<AuthNavGraph>(
        startDestination = SignInNav,
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
    navigate(AuthNavGraph) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}