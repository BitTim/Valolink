/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       HttpEngine.android.kt
 * Module:     Valolink.shared.androidMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 20:17
 */

package dev.bittim.valolink.core.data.remote

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun getHttpsEngine(): HttpClientEngine = OkHttp.create()