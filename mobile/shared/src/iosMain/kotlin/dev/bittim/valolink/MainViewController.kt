/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MainViewController.kt
 * Module:     Valolink.shared.iosMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 15:39
 */

package dev.bittim.valolink

import androidx.compose.ui.window.ComposeUIViewController
import dev.bittim.valolink.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }