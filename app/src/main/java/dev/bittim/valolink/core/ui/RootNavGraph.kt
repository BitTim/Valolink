package dev.bittim.valolink.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.auth.ui.AuthNavRoute
import dev.bittim.valolink.feature.auth.ui.authNavGraph
import dev.bittim.valolink.feature.auth.ui.navToAuthGraph
import dev.bittim.valolink.feature.content.ui.main.MainNavRoute
import dev.bittim.valolink.feature.content.ui.main.mainScreen
import dev.bittim.valolink.feature.content.ui.main.navToContentMain

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainNavRoute
    ) {
        authNavGraph(
            navController = navController,
            onNavToContentGraph = { navController.navToContentMain(AuthNavRoute) },
            onNavToOnboardingGraph = { navController.navToContentMain(AuthNavRoute) } // TODO: Change to Onboarding graph
        )

        mainScreen(
            onNavAuthGraph = { navController.navToAuthGraph(MainNavRoute) }
        )
    }
}