package dev.bittim.valolink.feature.content.ui.main.components.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navItems = listOf(
        NavBarItem.Home, NavBarItem.Contracts, NavBarItem.Matches, NavBarItem.Friends
    )

    val isNavBarDestination = navItems.any { it.route == currentDestination?.route }
    if (!isNavBarDestination) return

    NavigationBar {
        navItems.forEach {
            AddNavItem(
                navItem = it,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddNavItem(
    navItem: NavBarItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == navItem.route
    } == true

    NavigationBarItem(
        icon = if (isSelected) navItem.activeIcon else navItem.inactiveIcon,
        label = { Text(text = navItem.title) },
        selected = isSelected,
        onClick = { navItem.navigation(navController, currentDestination?.route ?: "") },
        alwaysShowLabel = false
    )
}