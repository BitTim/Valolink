package dev.bittim.valolink.feature.content.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.content.ui.contracts.contractsScreen
import dev.bittim.valolink.feature.content.ui.friends.friendsScreen
import dev.bittim.valolink.feature.content.ui.home.HomeNavRoute
import dev.bittim.valolink.feature.content.ui.home.homeScreen
import dev.bittim.valolink.feature.content.ui.matches.matchesScreen

@Composable
fun ContentNavGraph(
    navController: NavHostController,
    onSignOutClicked: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HomeNavRoute
    ) {
        homeScreen(
            onSignOutClicked = onSignOutClicked
        )
        contractsScreen()
        matchesScreen()
        friendsScreen()
    }
}