/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           CreateSupabaseClientUseCase.kt
Author:         Tim Anhalt (BitTim)
Created:        14.10.2024
Description:    Handles the creation of the Supabase client.
*/

package dev.bittim.valolink.core.domain.usecase.supabase

import dev.bittim.valolink.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.realtime.Realtime

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
            }
            install(Realtime)
            install(Functions)
        }
    }
}