package dev.bittim.valolink.core.ui.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.bittim.valolink.core.ui.nav.Routes
import dev.bittim.valolink.feature.auth.ui.signin.SignInScreen

fun NavGraphBuilder.authNavGraph() {
    navigation(
        route = Routes.Auth.route,
        startDestination = Routes.Auth.SignIn.route
    ) {
        composable(Routes.Auth.SignIn.route) {
            SignInScreen()
        }
    }
}