/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AppModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 01:51
 */

package dev.bittim.valolink.core.di

import dev.bittim.valolink.core.data.remote.createSupabaseClient
import dev.bittim.valolink.core.data.repo.SupabaseAuthRepo
import dev.bittim.valolink.core.domain.repo.AuthRepo
import dev.bittim.valolink.core.domain.usecase.ObserverSessionStatusUseCase
import dev.bittim.valolink.core.domain.usecase.SignInWithOtpUseCase
import dev.bittim.valolink.core.domain.usecase.SubmitOtpUseCase
import dev.bittim.valolink.core.ui.screen.root.RootScreenViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("AppScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single { createSupabaseClient() }

    single { get<SupabaseClient>().postgrest }
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().realtime }
    single { get<SupabaseClient>().storage }

    single<AuthRepo> { SupabaseAuthRepo(get(), get(named("AppScope"))) }

    singleOf(::ObserverSessionStatusUseCase)
    singleOf(::SignInWithOtpUseCase)
    singleOf(::SubmitOtpUseCase)

    viewModelOf(::RootScreenViewModel)
}