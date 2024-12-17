/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingContainer.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.12.24, 21:00
 */


package dev.bittim.valolink.onboarding.ui.container

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.onboarding.ui.components.OnboardingHeader
import dev.bittim.valolink.onboarding.ui.screens.createAccount.CreateAccountNav
import dev.bittim.valolink.onboarding.ui.screens.createAccount.createAccountScreen
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.landing.landingScreen
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.PasswordForgotNav
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.passwordForgotScreen
import dev.bittim.valolink.onboarding.ui.screens.signin.SigninNav
import dev.bittim.valolink.onboarding.ui.screens.signin.signinScreen
import kotlinx.serialization.InternalSerializationApi

@OptIn(ExperimentalSharedTransitionApi::class, InternalSerializationApi::class)
@Composable
fun OnboardingContainer(
    state: OnboardingContainerState,
    navController: NavHostController,
    onNavToMain: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        topBar = {
            OnboardingHeader(
                modifier = Modifier.fillMaxWidth(),
                title = state.title?.asString() ?: "",
                progress = state.progress ?: 0f,
                description = state.description?.asString() ?: "",
            )
        }
    ) {
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            startDestination = LandingNav,
        ) {
            landingScreen(
                onNavToAuth = onNavToMain,
                onNavToSignin = { navController.navigate(SigninNav) }
            )

            signinScreen(
                onNavBack = { navController.navigateUp() },
                onNavToPasswordForgot = { navController.navigate(PasswordForgotNav) },
                onNavToCreateAccount = { navController.navigate(CreateAccountNav) }
            )

            passwordForgotScreen(
                onNavBack = { navController.navigateUp() }
            )

            createAccountScreen(
                onNavBack = { navController.navigateUp() }
            )
        }
    }
}