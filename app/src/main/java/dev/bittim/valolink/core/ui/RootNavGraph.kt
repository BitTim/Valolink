package dev.bittim.valolink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.auth.ui.authNavGraph
import dev.bittim.valolink.feature.auth.ui.navToAuthGraph
import dev.bittim.valolink.feature.content.ui.main.MainNavRoute
import dev.bittim.valolink.feature.content.ui.main.mainScreen
import dev.bittim.valolink.feature.content.ui.main.navToContentMain
import dev.bittim.valolink.ui.theme.Transition

@Composable
fun RootNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainNavRoute,
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        authNavGraph(
            navController = navController,
            onNavToContentGraph = { navController.navToContentMain() },
            onNavToOnboardingGraph = { navController.navToContentMain() } // TODO: Change to Onboarding graph
        )

        mainScreen(
            onNavAuthGraph = { navController.navToAuthGraph() }
        )
    }
}