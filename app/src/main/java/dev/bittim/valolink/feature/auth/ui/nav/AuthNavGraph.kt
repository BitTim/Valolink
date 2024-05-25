package dev.bittim.valolink.feature.auth.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.feature.main.ui.nav.navToMainGraph
import kotlinx.serialization.Serializable

@Serializable
object AuthNavGraph

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
) {
    navigation<AuthNavGraph>(
        startDestination = SignInNav,
    ) {
        signInScreen(onNavToContent = { navController.navToMainGraph() },
                     onNavToSignUp = { navController.navToSignUp() })

        signUpScreen(onNavToContent = { navController.navToMainGraph() },
                     onNavToSignIn = { navController.popBackStack() })
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