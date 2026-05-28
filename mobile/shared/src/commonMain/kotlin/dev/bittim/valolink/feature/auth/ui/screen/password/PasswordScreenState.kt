/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       PasswordScreenState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.05.26, 20:52
 */

package dev.bittim.valolink.feature.auth.ui.screen.password

import org.jetbrains.compose.resources.StringResource

data class PasswordScreenState(
    val error: StringResource? = null
)
