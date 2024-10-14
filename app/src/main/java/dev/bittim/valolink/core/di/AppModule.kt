/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           AppModule.kt
Author:         Tim Anhalt (BitTim)
Created:        25.03.2024
Description:    Handles dependency injection for the app.
*/

package dev.bittim.valolink.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.core.domain.usecase.supabase.CreateSupabaseClientUseCase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // ================================
    //  Supabase
    // ================================

    @Provides
    @Singleton
    fun provideSupabaseClient(createSupabaseClientUseCase: CreateSupabaseClientUseCase): SupabaseClient {
        return createSupabaseClientUseCase()
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideSupabaseRealtime(client: SupabaseClient): Realtime {
        return client.realtime
    }

    @Provides
    @Singleton
    fun provideSupabaseFunctions(client: SupabaseClient): Functions {
        return client.functions
    }

    // ================================
    //  UseCases
    // ================================

    @Provides
    @Singleton
    fun provideCreateSupabaseClientUseCase(): CreateSupabaseClientUseCase {
        return CreateSupabaseClientUseCase()
    }
}