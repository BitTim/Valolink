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
import io.github.jan.supabase.auth.Auth
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
        auth: Auth,
    ): AuthRepository {
        return AuthSupabaseRepository(auth)
    }

    @Provides
    @Singleton
    fun provideUserLevelRepository(
        userDatabase: PowerSyncDatabase
    ): UserLevelRepository {
        return UserLevelPowersyncRepository(userDatabase)
    }

    @Provides
    @Singleton
    fun provideUserContractRepository(
        userDatabase: PowerSyncDatabase,
    ): UserContractRepository {
        return UserContractPowersyncRepository(userDatabase)
    }

    @Provides
    @Singleton
    fun provideUserAgentRepository(
        userDatabase: PowerSyncDatabase,
    ): UserAgentRepository {
        return UserAgentPowersyncRepository(userDatabase)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDatabase: PowerSyncDatabase,
    ): UserRepository {
        return UserPowersyncRepository(userDatabase)
    }
}
