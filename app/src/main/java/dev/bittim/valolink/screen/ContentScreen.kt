package dev.bittim.valolink.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.bittim.valolink.navgraph.MainNavGraph
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun ContentScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold (
        bottomBar = { BottomBar(navController = navController) }
    ) {
        MainNavGraph(
            navController = navController,
            paddingValues = it
        )
    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        ContentScreen.HOME,
        ContentScreen.CONTRACTS,
        ContentScreen.MATCHES,
        ContentScreen.FRIENDS
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if(bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddNavItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddNavItem(
    screen: ContentScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = if (currentDestination?.route == screen.route) screen.activeIcon else screen.inactiveIcon,
                contentDescription = null
            )
        },
        alwaysShowLabel = false,
        selected = currentDestination?.route == screen.route,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    ValolinkTheme {
        ContentScreen()
    }
}