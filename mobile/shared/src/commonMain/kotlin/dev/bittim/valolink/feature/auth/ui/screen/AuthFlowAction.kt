/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 21:26
 */

package dev.bittim.valolink.feature.auth.ui.screen

sealed interface AuthFlowAction {
    data class Back(val navBack: () -> Unit): AuthFlowAction
    data class EmailChange(val email: String): AuthFlowAction
    data object EmailContinue: AuthFlowAction
    data class OtpChange(val otp: String): AuthFlowAction
    data object OtpContinue : AuthFlowAction
}