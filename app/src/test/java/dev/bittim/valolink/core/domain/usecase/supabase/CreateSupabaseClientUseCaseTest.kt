/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           CreateSupabaseClientUseCaseTest.kt
Author:         Tim Anhalt (BitTim)
Created:        14.10.2024
Description:    Contains tests for CreateSupabaseClientUseCase.kt
*/

package dev.bittim.valolink.core.domain.usecase.supabase

import dev.bittim.valolink.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.ktor.util.reflect.instanceOf
import org.junit.Test

class CreateSupabaseClientUseCaseTest {
    @Test
    fun invoke_returnsSupabaseClient() {
        val createSupabaseClientUseCase = CreateSupabaseClientUseCase()
        val supabaseClient = createSupabaseClientUseCase()

        assert(supabaseClient.instanceOf(SupabaseClient::class))
        assert(supabaseClient.supabaseUrl == BuildConfig.SUPABASE_URL)
        assert(supabaseClient.supabaseKey == BuildConfig.SUPABASE_ANON_KEY)
    }
}