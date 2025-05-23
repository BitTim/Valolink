/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:29
 */

package dev.bittim.valolink.onboarding.ui.screens

import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.onboarding.ui.screens.contractSetup.ContractSetupNav
import dev.bittim.valolink.onboarding.ui.screens.createAccount.CreateAccountNav
import dev.bittim.valolink.onboarding.ui.screens.finish.FinishNav
import dev.bittim.valolink.onboarding.ui.screens.landing.LandingNav
import dev.bittim.valolink.onboarding.ui.screens.passwordForgot.PasswordForgotNav
import dev.bittim.valolink.onboarding.ui.screens.passwordReset.PasswordResetNav
import dev.bittim.valolink.onboarding.ui.screens.profileSetup.ProfileSetupNav
import dev.bittim.valolink.onboarding.ui.screens.rankSetup.RankSetupNav
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
    ),

    PasswordReset(
        route = PasswordResetNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_passwordReset_title),
        description = UiText.StringResource(R.string.onboarding_passwordReset_description),
        step = 1
    ),

    ProfileSetup(
        route = ProfileSetupNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_profileSetup_title),
        description = UiText.StringResource(R.string.onboarding_profileSetup_description),
        step = 2
    ),

    RankSetup(
        route = RankSetupNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_rankSetup_title),
        description = UiText.StringResource(R.string.onboarding_rankSetup_description),
        step = 3
    ),

    ContractSetup(
        route = ContractSetupNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_contractSetup_title),
        description = UiText.StringResource(R.string.onboarding_contractSetup_description),
        step = 4
    ),

    Finish(
        route = FinishNav::class.java.name,
        title = UiText.StringResource(R.string.onboarding_finish_title),
        description = UiText.StringResource(R.string.onboarding_finish_description),
        step = 5
    );

    companion object {
        const val STEP_OFFSET = 2

        fun getMaxStep(): Int {
            return entries.maxOf { it.step }
        }
    }
}
