/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.12.24, 17:29
 */

package dev.bittim.valolink.onboarding.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import dev.bittim.valolink.main.ui.nav.navToMainGraph
import dev.bittim.valolink.onboarding.ui.screens.createAccount.CreateAccountNav
import dev.bittim.valolink.onboarding.ui.screens.createAccount.createAccountScreen
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.landing.landingScreen
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.PasswordForgotNav
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.passwordForgotScreen
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
            onNavToPasswordForgot = { navController.navigate(PasswordForgotNav) },
            onNavToCreateAccount = { navController.navigate(CreateAccountNav) }
        )

        passwordForgotScreen(onNavBack = { navController.navigateUp() })

        createAccountScreen(onNavBack = { navController.navigateUp() })
    }
}