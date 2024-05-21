package dev.bittim.valolink.feature.content.ui.screens.main.components.navbar

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
import dev.bittim.valolink.feature.content.ui.nav.contracts.navToContractsGraph
import dev.bittim.valolink.feature.content.ui.nav.navToFriends
import dev.bittim.valolink.feature.content.ui.nav.navToHome
import dev.bittim.valolink.feature.content.ui.nav.navToMatches

enum class NavItem(
    val title: String,
    val activeIcon: @Composable () -> Unit,
    val inactiveIcon: @Composable () -> Unit, val navigation: (NavController) -> Unit
) {
    Home(
        title = "Home",
        activeIcon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
        inactiveIcon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home") },
        navigation = NavController::navToHome
    ),

    Contracts(
        title = "Contracts",
        activeIcon = { Icon(imageVector = Icons.Filled.Map, contentDescription = "Map") },
        inactiveIcon = { Icon(imageVector = Icons.Outlined.Map, contentDescription = "Map") },
        navigation = NavController::navToContractsGraph
    ),

    Matches(
        title = "Matches",
        activeIcon = { Icon(imageVector = Icons.Filled.History, contentDescription = "History") },
        inactiveIcon = {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "History"
            )
        },
        navigation = NavController::navToMatches
    ),

    Friends(
        title = "Friends",
        activeIcon = { Icon(imageVector = Icons.Filled.People, contentDescription = "People") },
        inactiveIcon = { Icon(imageVector = Icons.Outlined.People, contentDescription = "People") },
        navigation = NavController::navToFriends
    ),
}