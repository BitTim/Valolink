/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           AppModuleTest.kt
Author:         Tim Anhalt (BitTim)
Created:        14.10.2024
Description:    Contains tests for AppModule.kt
*/

package dev.bittim.valolink.core.di

import dev.bittim.valolink.core.domain.usecase.supabase.CreateSupabaseClientUseCase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime
import io.ktor.util.reflect.instanceOf
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AppModuleTest {
    @Test
    fun provideCreateSupabaseClientUseCase_returnsCreateSupabaseClientUseCase() {
        assert(
            AppModule
                .provideCreateSupabaseClientUseCase()
                .instanceOf(CreateSupabaseClientUseCase::class)
        )
    }

    @Test
    fun provideSupabaseClient_returnsSupabaseClient() {
        assertEquals(
            CreateSupabaseClientUseCase().invoke(),
            AppModule.provideSupabaseClient(CreateSupabaseClientUseCase())
        )
    }

    @Test
    fun provideSupabaseDatabase_returnsSupabaseDatabase() {
        val supabaseClient = CreateSupabaseClientUseCase().invoke()
        assertEquals(
            supabaseClient.postgrest,
            AppModule.provideSupabaseDatabase(supabaseClient)
        )
    }

    @Test
    fun provideSupabaseAuth_returnsSupabaseAuth() {
        val supabaseClient = CreateSupabaseClientUseCase().invoke()
        assertEquals(
            supabaseClient.auth,
            AppModule.provideSupabaseAuth(supabaseClient)
        )
    }

    @Test
    fun provideSupabaseRealtime_returnsSupabaseRealtime() {
        val supabaseClient = CreateSupabaseClientUseCase().invoke()
        assertEquals(
            supabaseClient.realtime,
            AppModule.provideSupabaseRealtime(supabaseClient)
        )
    }

    @Test
    fun provideSupabaseFunctions_returnsSupabaseFunctions() {
        val supabaseClient = CreateSupabaseClientUseCase().invoke()
        assertEquals(
            supabaseClient.functions,
            AppModule.provideSupabaseFunctions(supabaseClient)
        )
    }
}