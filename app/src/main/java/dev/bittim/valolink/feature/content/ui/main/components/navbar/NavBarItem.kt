package dev.bittim.valolink.feature.content.ui.main.components.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dev.bittim.valolink.feature.content.ui.contracts.ContractsNavRoute
import dev.bittim.valolink.feature.content.ui.contracts.gearlist.ContractsGearListNavRoute
import dev.bittim.valolink.feature.content.ui.contracts.main.ContractsMainNavRoute
import dev.bittim.valolink.feature.content.ui.contracts.navToContractsGraph
import dev.bittim.valolink.feature.content.ui.friends.FriendsNavRoute
import dev.bittim.valolink.feature.content.ui.friends.navToFriends
import dev.bittim.valolink.feature.content.ui.home.HomeNavRoute
import dev.bittim.valolink.feature.content.ui.home.navToHome
import dev.bittim.valolink.feature.content.ui.matches.MatchesNavRoute
import dev.bittim.valolink.feature.content.ui.matches.navToMatches

sealed class NavBarItem(
    val title: String,
    val route: String, val nestedRoutes: List<String> = listOf(),
    val activeIcon: @Composable () -> Unit,
    val inactiveIcon: @Composable () -> Unit, val navigation: (NavController) -> Unit
) {
    data object Home : NavBarItem(
        title = "Home",
        route = HomeNavRoute,
        activeIcon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
        inactiveIcon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home") },
        navigation = NavController::navToHome
    )

    data object Contracts : NavBarItem(
        title = "Contracts",
        route = ContractsNavRoute,
        nestedRoutes = listOf(ContractsMainNavRoute, ContractsGearListNavRoute),
        activeIcon = { Icon(imageVector = Icons.Filled.Map, contentDescription = "Map") },
        inactiveIcon = { Icon(imageVector = Icons.Outlined.Map, contentDescription = "Map") },
        navigation = NavController::navToContractsGraph
    )

    data object Matches : NavBarItem(
        title = "Matches",
        route = MatchesNavRoute,
        activeIcon = { Icon(imageVector = Icons.Filled.History, contentDescription = "History") },
        inactiveIcon = {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "History"
            )
        },
        navigation = NavController::navToMatches
    )

    data object Friends : NavBarItem(
        title = "Friends",
        route = FriendsNavRoute,
        activeIcon = { Icon(imageVector = Icons.Filled.People, contentDescription = "People") },
        inactiveIcon = { Icon(imageVector = Icons.Outlined.People, contentDescription = "People") },
        navigation = NavController::navToFriends
    )
}