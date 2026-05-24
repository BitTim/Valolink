/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AppModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:56
 */

package dev.bittim.valolink.core.di

import dev.bittim.valolink.core.data.remote.createSupabaseClient
import dev.bittim.valolink.core.domain.usecase.ObserverSessionStatusUseCase
import dev.bittim.valolink.core.ui.screen.root.RootScreenViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.storage
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { createSupabaseClient() }

    single { get<SupabaseClient>().postgrest }
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().realtime }
    single { get<SupabaseClient>().storage }

    singleOf(::ObserverSessionStatusUseCase)

    viewModelOf(::RootScreenViewModel)
}