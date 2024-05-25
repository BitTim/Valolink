package dev.bittim.valolink.feature.content.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.feature.content.ui.nav.contracts.contractsNavGraph
import dev.bittim.valolink.feature.content.ui.nav.onboarding.navToOnboardingGraph
import dev.bittim.valolink.feature.content.ui.nav.onboarding.onboardingNavGraph
import dev.bittim.valolink.ui.theme.Transition

@Composable
fun ContentNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    onSignOutClicked: () -> Unit,
) {
    NavHost(modifier = modifier,
            navController = navController,
            startDestination = HomeNav,
            enterTransition = { Transition.topLevelEnter },
            exitTransition = { Transition.topLevelExit },
            popEnterTransition = { Transition.topLevelEnter },
            popExitTransition = { Transition.topLevelExit }) {
        homeScreen(onSignOutClicked = onSignOutClicked,
                   navToOnboarding = { navController.navToOnboardingGraph() })
        contractsNavGraph(navController)
        matchesScreen()
        friendsScreen()

        onboardingNavGraph(navController) { navController.navToHome() }
    }
}