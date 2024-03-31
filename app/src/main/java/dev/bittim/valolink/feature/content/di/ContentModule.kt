package dev.bittim.valolink.feature.content.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.feature.content.data.repository.FirebaseUserRepository
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentModule {
    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth): UserRepository {
        return FirebaseUserRepository(auth)
    }
}