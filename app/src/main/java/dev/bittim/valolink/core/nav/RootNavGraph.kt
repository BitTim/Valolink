package dev.bittim.valolink.core.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.auth.ui.AuthNavRoute
import dev.bittim.valolink.feature.auth.ui.authNavGraph
import dev.bittim.valolink.feature.auth.ui.navToAuthGraph
import dev.bittim.valolink.feature.content.ui.ContentNavRoute
import dev.bittim.valolink.feature.content.ui.contentNavGraph
import dev.bittim.valolink.feature.content.ui.navToContentGraph

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ContentNavRoute
    ) {
        authNavGraph(
            navController = navController,
            onNavToContentGraph = { navController.navToContentGraph(AuthNavRoute) },
            onNavToOnboardingGraph = { navController.navToContentGraph(AuthNavRoute) } // TODO: Change to Onboarding graph
        )

        contentNavGraph(
            navController = navController,
            onNavAuthGraph = { navController.navToAuthGraph(ContentNavRoute) }
        )
    }
}