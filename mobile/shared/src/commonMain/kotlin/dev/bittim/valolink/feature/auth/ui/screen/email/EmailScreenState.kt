/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       EmailScreenState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:06
 */

package dev.bittim.valolink.feature.auth.ui.screen.email

import org.jetbrains.compose.resources.StringResource

data class EmailScreenState(
    val email: String = "",
    val error: StringResource? = null
)
