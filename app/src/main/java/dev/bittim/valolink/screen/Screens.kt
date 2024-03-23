package dev.bittim.valolink.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.ui.graphics.vector.ImageVector

enum class AuthScreen(
    val route: String
) {
    SIGNIN("login"),
    SIGNUP("register"),
    FORGOT_PASSWORD("forgot_password")
}

enum class ContentScreen(
    val title: String,
    val route: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    HOME("Home", "home", Icons.Filled.Home, Icons.Outlined.Home),
    CONTRACTS("Contracts", "contracts", Icons.Filled.Map, Icons.Outlined.Map),
    MATCHES("Matches", "matches", Icons.Filled.History, Icons.Outlined.History),
    FRIENDS("Friends", "friends", Icons.Filled.People, Icons.Outlined.People)
}