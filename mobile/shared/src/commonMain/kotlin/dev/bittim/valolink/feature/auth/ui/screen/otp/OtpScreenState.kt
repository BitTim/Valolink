/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OtpScreenState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:09
 */

package dev.bittim.valolink.feature.auth.ui.screen.otp

import org.jetbrains.compose.resources.StringResource

data class OtpScreenState(
    val otp: String = "",
    val error: StringResource? = null
)
