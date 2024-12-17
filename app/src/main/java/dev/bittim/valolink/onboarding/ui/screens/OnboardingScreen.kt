/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.12.24, 20:57
 */

package dev.bittim.valolink.onboarding.ui.screens

import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.onboarding.ui.screens.createAccount.CreateAccountNav
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.PasswordForgotNav
import dev.bittim.valolink.onboarding.ui.screens.signin.SigninNav

enum class OnboardingScreen(
    val route: String,
    val title: UiText,
    val description: UiText,
    val step: Int,
) {
    Landing(
        route = LandingNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_landing_title),
        description = UiText.StringResource(R.string.onboarding_landing_description),
        step = 0
    ),

    Signin(
        route = SigninNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_signin_title),
        description = UiText.StringResource(R.string.onboarding_signin_description),
        step = 1
    ),

    CreateAccount(
        route = CreateAccountNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_createAccount_title),
        description = UiText.StringResource(R.string.onboarding_createAccount_description),
        step = 1
    ),

    PasswordForgot(
        route = PasswordForgotNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_passwordForgot_title),
        description = UiText.StringResource(R.string.onboarding_passwordForgot_description),
        step = 1
    );

    companion object {
        fun getMaxStep(): Int {
            return entries.maxOf { it.step }
        }
    }
}