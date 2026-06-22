/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseDeeplinkHandler.ios.kt
 * Module:     Valolink.shared.iosMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 15:06
 */

@file:Suppress("unused")

package dev.bittim.valolink.core.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.mp.KoinPlatformTools
import platform.Foundation.NSURL

object SupabaseDeeplinkHandlerIos {
    private val supabase by lazy {
        KoinPlatformTools.defaultContext().get().get<SupabaseClient>()
    }

    fun handleDeeplink(url: NSURL) {
        supabase.handleDeeplinks(url)
    }
}