package dev.bittim.valolink.onboarding.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import dev.bittim.valolink.main.ui.nav.navToMainGraph
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.landing.landingScreen
import kotlinx.serialization.Serializable

@Serializable
object OnboardingNavGraph

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController
) {
    navigation<OnboardingNavGraph>(
        startDestination = LandingNav,
    ) {
        landingScreen(onNavToAuth = { navController.navToMainGraph() })
    }
}