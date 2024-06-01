package dev.bittim.valolink.main.ui.nav.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.main.ui.nav.content.navToContentContainer
import kotlinx.serialization.Serializable

@Serializable
object OnboardingNavGraph

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController,
) {
    navigation<OnboardingNavGraph>(
        startDestination = GetStartedNav,
    ) {
        getStartedScreen(onNavToContent = { navController.navToContentContainer() })
    }
}

fun NavController.navToOnboardingGraph() {
    navigate(OnboardingNavGraph) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}