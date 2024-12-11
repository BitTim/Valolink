package dev.bittim.valolink.onboarding.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import dev.bittim.valolink.main.ui.nav.navToMainGraph
import dev.bittim.valolink.onboarding.ui.screens.createAccount.CreateAccountNav
import dev.bittim.valolink.onboarding.ui.screens.createAccount.createAccountScreen
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.landing.landingScreen
import dev.bittim.valolink.onboarding.ui.screens.signin.SigninNav
import dev.bittim.valolink.onboarding.ui.screens.signin.signinScreen
import kotlinx.serialization.Serializable

@Serializable
object OnboardingNavGraph

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController
) {
    navigation<OnboardingNavGraph>(
        startDestination = LandingNav,
    ) {
        landingScreen(
            onNavToAuth = { navController.navToMainGraph() },
            onNavToSignin = { navController.navigate(SigninNav) }
        )

        signinScreen(
            onNavBack = { navController.navigateUp() },
            onNavToCreateAccount = { navController.navigate(CreateAccountNav) }
        )

        createAccountScreen(onNavBack = { navController.navigateUp() })
    }
}