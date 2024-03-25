package dev.bittim.valolink.feature.auth.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.feature.auth.data.AuthRepository
import dev.bittim.valolink.feature.auth.data.FirebaseAuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return FirebaseAuthRepository(auth)
    }
}