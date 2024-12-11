package dev.bittim.valolink.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.auth.data.repository.AuthRepository
import dev.bittim.valolink.auth.data.repository.SupabaseAuthRepository
import io.github.jan.supabase.auth.Auth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthRepository {
        return SupabaseAuthRepository(auth)
    }
}