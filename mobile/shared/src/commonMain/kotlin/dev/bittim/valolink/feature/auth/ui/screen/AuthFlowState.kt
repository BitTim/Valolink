/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 21:25
 */

package dev.bittim.valolink.feature.auth.ui.screen

import dev.bittim.valolink.core.ui.components.SpinDirection
import org.jetbrains.compose.resources.StringResource

data class AuthFlowState(
    val step: AuthFlowStep = AuthFlowStep.LandingStep,
    val heroIconSpinDirection: SpinDirection = SpinDirection.None,

    val email: String = "",
    val emailError: StringResource? = null,

    val otp: String = "",
    val otpError: StringResource? = null
)
