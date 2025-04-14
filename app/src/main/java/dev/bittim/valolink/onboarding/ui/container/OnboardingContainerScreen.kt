/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingContainerScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */


package dev.bittim.valolink.onboarding.ui.container

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.onboarding.ui.components.OnboardingHeader
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.onboarding.ui.screens.createAccount.createAccountScreen
import dev.bittim.valolink.onboarding.ui.screens.createAccount.navOnboardingCreateAccount
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.landing.landingScreen
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.navOnboardingPasswordForgot
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.passwordForgotScreen
import dev.bittim.valolink.onboarding.ui.screens.passwordReset.passwordResetScreen
import dev.bittim.valolink.onboarding.ui.screens.profileSetup.navOnboardingProfileSetup
import dev.bittim.valolink.onboarding.ui.screens.profileSetup.profileSetupScreen
import dev.bittim.valolink.onboarding.ui.screens.signin.navOnboardingSignin
import dev.bittim.valolink.onboarding.ui.screens.signin.signinScreen
import kotlinx.serialization.InternalSerializationApi

@OptIn(ExperimentalSharedTransitionApi::class, InternalSerializationApi::class)
@Composable
fun OnboardingContainerScreen(
    state: OnboardingContainerState,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    navContent: () -> Unit,
) {
    if (state.isAuthenticated) {
        val step = state.onboardingStep + OnboardingScreen.stepOffset
        val route = OnboardingScreen.entries.find { e -> e.step == step }?.route

        if (route != null) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeContent,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            OnboardingHeader(
                modifier = Modifier.fillMaxWidth(),
                title = state.title?.asString() ?: "",
                progress = state.progress ?: 0f,
                description = state.description?.asString() ?: "",
            )

            Spacer(modifier = Modifier.height(Spacing.m))

            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = LandingNav,
                enterTransition = { Transition.ForwardBackward.enter },
                exitTransition = { Transition.ForwardBackward.exit },
                popEnterTransition = { Transition.ForwardBackward.popEnter },
                popExitTransition = { Transition.ForwardBackward.popExit }
            ) {
                landingScreen(
                    navSignin = {
                        navController.navOnboardingSignin()
                    },
                    navLocalMode = {
                        navController.navOnboardingProfileSetup()
                    }
                )

                signinScreen(
                    navBack = { navController.navigateUp() },
                    navMain = navContent,
                    navPasswordForgot = { navController.navOnboardingPasswordForgot() },
                    navCreateAccount = { navController.navOnboardingCreateAccount() },
                    snackbarHostState = snackbarHostState,
                )

                passwordForgotScreen(
                    navBack = { navController.navigateUp() },
                    snackbarHostState = snackbarHostState,
                )

                passwordResetScreen(
                    navBack = { navController.navigateUp() },
                    navMain = navContent,
                    snackbarHostState = snackbarHostState
                )

                createAccountScreen(
                    navBack = { navController.navigateUp() },
                    navProfileSetup = { navController.navOnboardingProfileSetup() },
                    snackbarHostState = snackbarHostState,
                )

                profileSetupScreen(
                    navBack = { navController.navigateUp() },
                    navRankSetup = { navContent() },
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}
