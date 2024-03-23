package dev.bittim.valolink.navgraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.bittim.valolink.screen.ContentScreen
import dev.bittim.valolink.screen.content.ContractsScreen
import dev.bittim.valolink.screen.content.FriendsScreen
import dev.bittim.valolink.screen.content.HomeScreen
import dev.bittim.valolink.screen.content.MatchesScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        route = NavGraph.CONTENT.route,
        startDestination = ContentScreen.HOME.route
    ) {
        composable(ContentScreen.HOME.route) {
            HomeScreen()
        }
        composable(ContentScreen.CONTRACTS.route) {
            ContractsScreen()
        }
        composable(ContentScreen.MATCHES.route) {
            MatchesScreen()
        }
        composable(ContentScreen.FRIENDS.route) {
            FriendsScreen()
        }
    }
}