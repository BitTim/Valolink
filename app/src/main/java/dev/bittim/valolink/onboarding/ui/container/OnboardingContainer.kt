/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingContainer.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   18.12.24, 02:14
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.Transition
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
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeContent,
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
}