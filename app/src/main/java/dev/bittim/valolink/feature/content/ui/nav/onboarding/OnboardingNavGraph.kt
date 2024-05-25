package dev.bittim.valolink.feature.content.ui.nav.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object OnboardingNavGraph

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController,
    onNavToContentGraph: () -> Unit,
) {
    navigation<OnboardingNavGraph>(
        startDestination = GetStartedNav,
    ) {
        getStartedScreen(
            onNavToContent = onNavToContentGraph
        )
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