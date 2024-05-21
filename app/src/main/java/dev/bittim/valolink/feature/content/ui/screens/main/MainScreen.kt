package dev.bittim.valolink.feature.content.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.bittim.valolink.feature.content.ui.nav.ContentNavGraph
import dev.bittim.valolink.feature.content.ui.screens.main.components.navbar.NavItem

@Composable
fun MainScreen(
    state: MainState,
    navController: NavHostController,
    onSignOutClicked: () -> Unit,
    onNavAuthGraph: () -> Unit
) {
    if (!state.isAuthenticated) {
        onNavAuthGraph()
    }

    var currentDestination by rememberSaveable { mutableStateOf(NavItem.Home) }

    NavigationSuiteScaffold(modifier = Modifier.fillMaxSize(), navigationSuiteItems = {
        NavItem.entries.forEach {
            val isSelected = it == currentDestination

            item(icon = if (isSelected) it.activeIcon else it.inactiveIcon,
                label = { Text(it.title) },
                selected = isSelected,
                onClick = {
                    currentDestination = it
                    it.navigation(navController)
                })
        }
    }) {
        ContentNavGraph(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            onSignOutClicked = onSignOutClicked
        )
    }
}