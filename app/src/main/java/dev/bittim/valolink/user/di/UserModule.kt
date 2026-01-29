/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserModule.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.di

import android.content.Context
import androidx.work.WorkManager
import com.powersync.DatabaseDriverFactory
import com.powersync.PowerSyncDatabase
import com.powersync.connector.supabase.SupabaseConnector
import com.powersync.db.schema.Schema
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.BuildConfig
import dev.bittim.valolink.user.data.db.UserSchema
import dev.bittim.valolink.user.data.flags.UserFlags
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.repository.auth.AuthRepository
import dev.bittim.valolink.user.data.repository.auth.AuthSupabaseRepository
import dev.bittim.valolink.user.data.repository.synced.UserAgentPowersyncRepository
import dev.bittim.valolink.user.data.repository.synced.UserAgentRepository
import dev.bittim.valolink.user.data.repository.synced.UserContractPowersyncRepository
import dev.bittim.valolink.user.data.repository.synced.UserContractRepository
import dev.bittim.valolink.user.data.repository.synced.UserLevelPowersyncRepository
import dev.bittim.valolink.user.data.repository.synced.UserLevelRepository
import dev.bittim.valolink.user.data.repository.synced.UserPowersyncRepository
import dev.bittim.valolink.user.data.repository.synced.UserRepository
import dev.bittim.valolink.user.data.retired.local.UserDatabase
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideUserSchema(): Schema {
        return UserSchema.schema
    }

    @Provides
    @Singleton
    fun provideDatabaseDriverFactory(@ApplicationContext context: Context): DatabaseDriverFactory {
        return DatabaseDriverFactory(context)
    }

    @Provides
    @Singleton
    fun provideSupabaseConnector(): SupabaseConnector {
        return SupabaseConnector(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY,
            powerSyncEndpoint = BuildConfig.POWERSYNC_URL,
            storageBucket = BuildConfig.SUPABASE_STORAGE_BUCKET
        )
    }

    @Provides
    @Singleton
    fun provideUserDatabase(
        driverFactory: DatabaseDriverFactory,
        schema: Schema
    ): PowerSyncDatabase {
        return PowerSyncDatabase(
            factory = driverFactory,
            schema = schema
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthRepository {
        return AuthSupabaseRepository(auth)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(
        @ApplicationContext
        context: Context,
        auth: Auth,
        userFlags: UserFlags,
        userDatabase: UserDatabase,
    ): AuthRepository {
        return AuthSupabaseRepository(
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
        return UserLevelPowersyncRepository(
            userDatabase, database, workManager
        )
    }

    @Provides
    @Singleton
    fun provideUserContractRepository(
        authRepository: AuthRepository,
        userLevelRepository: UserLevelRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserContractRepository {
        return UserContractPowersyncRepository(
            authRepository, userLevelRepository, userDatabase, database, workManager
        )
    }

    @Provides
    @Singleton
    fun provideUserAgentRepository(
        authRepository: AuthRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        workManager: WorkManager,
    ): UserAgentRepository {
        return UserAgentPowersyncRepository(
            authRepository, userDatabase, database, workManager
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext
        context: Context,
        authRepository: AuthRepository,
        userRankRepository: UserRankRepository,
        userDatabase: UserDatabase,
        database: Postgrest,
        storage: Storage,
        workManager: WorkManager,
    ): UserRepository {
        return UserPowersyncRepository(
            context,
            authRepository,
            userRankRepository,
            userDatabase,
            database,
            storage,
            workManager
        )
    }
}
