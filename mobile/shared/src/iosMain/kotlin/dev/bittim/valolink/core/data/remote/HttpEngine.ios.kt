/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       HttpEngine.ios.kt
 * Module:     Valolink.shared.iosMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 20:18
 */

package dev.bittim.valolink.core.data.remote

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun getHttpsEngine(): HttpClientEngine = Darwin.create()