package dev.bittim.valolink.feature.content.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.content.ui.contracts.contractsNavGraph
import dev.bittim.valolink.feature.content.ui.friends.friendsScreen
import dev.bittim.valolink.feature.content.ui.home.HomeNavRoute
import dev.bittim.valolink.feature.content.ui.home.homeScreen
import dev.bittim.valolink.feature.content.ui.matches.matchesScreen
import dev.bittim.valolink.ui.theme.Transition

@Composable
fun ContentNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    onSignOutClicked: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeNavRoute,
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        homeScreen(
            onSignOutClicked = onSignOutClicked
        )
        contractsNavGraph(navController)
        matchesScreen()
        friendsScreen()
    }
}