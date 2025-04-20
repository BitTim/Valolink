/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserModule.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
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
import dev.bittim.valolink.user.data.repository.data.UserRankRepository
import dev.bittim.valolink.user.data.repository.data.UserRankSupabaseRepository
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
            context, auth, userFlags, userDatabase
        )
    }

    @Provides
    @Singleton
    fun provideUserRankRepository(
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserRankRepository {
        return UserRankSupabaseRepository(
            userDatabase, database, workManager
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
        @ApplicationContext
        context: Context,
        sessionRepository: SessionRepository,
        userAgentRepository: UserAgentRepository,
        userContractRepository: UserContractRepository,
        userRankRepository: UserRankRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        storage: Storage,
        workManager: WorkManager,
    ): UserDataRepository {
        return UserDataSupabaseRepository(
            context,
            sessionRepository,
            userAgentRepository,
            userContractRepository,
            userRankRepository,
            userDatabase,
            database,
            storage,
            workManager
        )
    }
}
