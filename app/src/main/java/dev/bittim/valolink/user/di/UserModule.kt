/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserModule.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.user.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.user.data.flags.UserFlags
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.SessionSupabaseRepository
import dev.bittim.valolink.user.data.repository.auth.AuthRepository
import dev.bittim.valolink.user.data.repository.auth.SupabaseAuthRepository
import dev.bittim.valolink.user.data.repository.data.UserAgentRepository
import dev.bittim.valolink.user.data.repository.data.UserAgentSupabaseRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.data.repository.data.UserContractSupabaseRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import dev.bittim.valolink.user.data.repository.data.UserDataSupabaseRepository
import dev.bittim.valolink.user.data.repository.data.UserLevelRepository
import dev.bittim.valolink.user.data.repository.data.UserLevelSupabaseRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthRepository {
        return SupabaseAuthRepository(auth)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(
        @ApplicationContext
        context: Context,
        auth: Auth,
        storage: Storage,
        userFlags: UserFlags,
        userDatabase: UserDatabase,
    ): SessionRepository {
        return SessionSupabaseRepository(
            context, auth, storage, userFlags, userDatabase
        )
    }

    @Provides
    @Singleton
    fun provideUserLevelRepository(
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserLevelRepository {
        return UserLevelSupabaseRepository(
            userDatabase, database, workManager
        )
    }

    @Provides
    @Singleton
    fun provideUserContractRepository(
        sessionRepository: SessionRepository,
        userLevelRepository: UserLevelRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserContractRepository {
        return UserContractSupabaseRepository(
            sessionRepository, userLevelRepository, userDatabase, database, workManager
        )
    }

    @Provides
    @Singleton
    fun provideUserAgentRepository(
        sessionRepository: SessionRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserAgentRepository {
        return UserAgentSupabaseRepository(
            sessionRepository, userDatabase, database, workManager
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        sessionRepository: SessionRepository,
        userAgentRepository: UserAgentRepository,
        userContractRepository: UserContractRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserDataRepository {
        return UserDataSupabaseRepository(
            sessionRepository,
            userAgentRepository,
            userContractRepository,
            userDatabase,
            database,
            workManager
        )
    }
}
