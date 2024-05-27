package dev.bittim.valolink.feature.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.feature.auth.data.repository.AuthRepository
import dev.bittim.valolink.feature.auth.data.repository.SupabaseAuthRepository
import dev.bittim.valolink.feature.auth.data.validator.AndroidEmailPatternValidator
import dev.bittim.valolink.feature.auth.data.validator.EmailPatternValidator
import dev.bittim.valolink.feature.auth.domain.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.ValidatePasswordUseCase
import dev.bittim.valolink.feature.auth.domain.ValidateUsernameUseCase
import dev.bittim.valolink.feature.auth.domain.ValidationUseCases
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthRepository {
        return SupabaseAuthRepository(auth)
    }

    @Provides
    @Singleton
    fun provideEmailPatternValidator(): EmailPatternValidator {
        return AndroidEmailPatternValidator()
    }

    @Provides
    @Singleton
    fun provideValidationUseCases(emailPatternValidator: EmailPatternValidator): ValidationUseCases {
        return ValidationUseCases(
            ValidateEmailUseCase(emailPatternValidator),
            ValidateUsernameUseCase(),
            ValidatePasswordUseCase()
        )
    }
}