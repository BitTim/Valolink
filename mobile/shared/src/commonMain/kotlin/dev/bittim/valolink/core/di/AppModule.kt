/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AppModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:31
 */

package dev.bittim.valolink.core.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.local.DatabaseBuilder
import dev.bittim.valolink.core.data.remote.createSupabaseClient
import dev.bittim.valolink.core.data.repo.SupabaseAuthRepo
import dev.bittim.valolink.core.data.repo.SupabaseValoVersionRepo
import dev.bittim.valolink.core.data.sync.SyncManager
import dev.bittim.valolink.core.domain.repo.AuthRepo
import dev.bittim.valolink.core.domain.repo.ValoVersionRepo
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
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single(named("AppScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single(named("SyncScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single { createSupabaseClient() }

    single { get<SupabaseClient>().postgrest }
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().realtime }
    single { get<SupabaseClient>().storage }

    single<Database> {
        get<DatabaseBuilder>().get()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<AuthRepo> { SupabaseAuthRepo(get(), get(named("AppScope"))) }
    singleOf(::SupabaseValoVersionRepo).bind<ValoVersionRepo>()

    single<SyncManager> { SyncManager(get(named("SyncScope")), get()) }

    singleOf(::ObserverSessionStatusUseCase)
    singleOf(::SignInWithOtpUseCase)
    singleOf(::SubmitOtpUseCase)

    viewModelOf(::RootScreenViewModel)
}