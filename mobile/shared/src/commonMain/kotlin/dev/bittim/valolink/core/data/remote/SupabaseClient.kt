/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseClient.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 15:30
 */

package dev.bittim.valolink.core.data.remote

import dev.bittim.valolink.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

fun createSupabaseClient(): SupabaseClient = createSupabaseClient(
    supabaseUrl = BuildKonfig.SUPABASE_URL,
    supabaseKey = BuildKonfig.SUPABASE_KEY
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    install(Storage)
}