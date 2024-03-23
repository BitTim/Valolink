package dev.bittim.valolink.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.bittim.valolink.screen.ContentScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = NavGraph.ROOT.route,
        startDestination = NavGraph.AUTH.route
    ) {
        authNavGraph(navController)
        composable(NavGraph.CONTENT.route) {
            ContentScreen()
        }
    }
}

enum class NavGraph(val route: String) {
    ROOT("root"),
    AUTH("auth"),
    CONTENT("content")
}