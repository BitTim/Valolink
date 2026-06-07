/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 17:28
 */

package dev.bittim.valolink.feature.auth.ui.screen

sealed interface AuthFlowAction {
    data object Back: AuthFlowAction
    data class EmailChange(val email: String): AuthFlowAction
    data object EmailContinue: AuthFlowAction
    data class OtpChange(val otp: String): AuthFlowAction
    data object OtpResend : AuthFlowAction
    data object OtpContinue : AuthFlowAction
}