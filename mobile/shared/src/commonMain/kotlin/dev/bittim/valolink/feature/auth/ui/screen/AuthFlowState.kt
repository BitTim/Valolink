/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.06.26, 19:21
 */

package dev.bittim.valolink.feature.auth.ui.screen

import org.jetbrains.compose.resources.StringResource

data class AuthFlowState(
    val step: AuthFlowStep = AuthFlowStep.LandingStep,
    val heroIconRotation: Float = 0f,

    val email: String = "",
    val emailError: StringResource? = null,

    val otp: String = "",
    val otpError: StringResource? = null,

    val cooldownSecondsLeft: Int? = null,
    val lastSentEmail: String? = null
)
