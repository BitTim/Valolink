/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       CreateSupabaseClientUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 14:44
 */

package dev.bittim.valolink.core.domain.usecase.supabase

import dev.bittim.valolink.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

class CreateSupabaseClientUseCase {
    operator fun invoke(): SupabaseClient {
        return createSupabaseClient(
            BuildConfig.SUPABASE_URL,
            BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest) {
                propertyConversionMethod = PropertyConversionMethod.NONE
            }
            install(Auth) {
                flowType = FlowType.PKCE

                host = "www.valolink.app"
                scheme = "https"

                defaultExternalAuthAction = ExternalAuthAction.CustomTabs()
            }
            install(Storage)
            install(Realtime)
            install(Functions)
        }
    }
}
