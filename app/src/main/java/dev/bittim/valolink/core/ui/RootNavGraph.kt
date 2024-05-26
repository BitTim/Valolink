package dev.bittim.valolink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.auth.ui.nav.authNavGraph
import dev.bittim.valolink.feature.main.ui.nav.MainNavGraph
import dev.bittim.valolink.feature.main.ui.nav.mainNavGraph
import dev.bittim.valolink.ui.theme.Transition

@Composable
fun RootNavGraph(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(modifier = modifier,
            navController = navController,
            startDestination = MainNavGraph,
            enterTransition = { Transition.topLevelEnter },
            exitTransition = { Transition.topLevelExit },
            popEnterTransition = { Transition.topLevelEnter },
            popExitTransition = { Transition.topLevelExit }) {
        authNavGraph(navController = navController)
        mainNavGraph(navController = navController)
    }
}