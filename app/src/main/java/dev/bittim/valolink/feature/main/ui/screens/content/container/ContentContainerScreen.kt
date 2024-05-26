package dev.bittim.valolink.feature.main.ui.screens.content.container

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.bittim.valolink.feature.main.ui.nav.content.ContentNavGraph
import dev.bittim.valolink.feature.main.ui.screens.content.container.components.navbar.NavItem

@Composable
fun ContentContainerScreen(
    state: ContentContainerState,
    navController: NavHostController,
    navToAuthGraph: () -> Unit,
    navToOnboardingGraph: () -> Unit,
    onSignOutClicked: () -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(NavItem.Home) }

    if (state.isAuthenticated != null && !state.isAuthenticated) {
        LaunchedEffect(Unit) {
            navToAuthGraph()
        }
    }

    if (state.hasOnboarded != null && !state.hasOnboarded) {
        LaunchedEffect(Unit) {
            navToOnboardingGraph()
        }
    }

    NavigationSuiteScaffold(modifier = Modifier.fillMaxSize(),
                            navigationSuiteItems = {
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