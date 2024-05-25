package dev.bittim.valolink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.auth.ui.nav.authNavGraph
import dev.bittim.valolink.feature.auth.ui.nav.navToAuthGraph
import dev.bittim.valolink.feature.content.ui.nav.MainNav
import dev.bittim.valolink.feature.content.ui.nav.mainScreen
import dev.bittim.valolink.feature.content.ui.nav.navToContentMain
import dev.bittim.valolink.ui.theme.Transition

@Composable
fun RootNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = MainNav,
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        authNavGraph(
            navController = navController,
            onNavToContentGraph = { navController.navToContentMain() }
        )

        mainScreen(
            onNavAuthGraph = { navController.navToAuthGraph() }
        )
    }
}