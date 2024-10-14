package dev.bittim.valolink.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.auth.data.repository.AuthRepository
import dev.bittim.valolink.auth.data.repository.SupabaseAuthRepository
import dev.bittim.valolink.auth.data.validator.AndroidEmailPatternValidator
import dev.bittim.valolink.auth.data.validator.EmailPatternValidator
import dev.bittim.valolink.auth.domain.ValidateEmailUseCase
import dev.bittim.valolink.auth.domain.ValidatePasswordUseCase
import dev.bittim.valolink.auth.domain.ValidateUsernameUseCase
import dev.bittim.valolink.auth.domain.ValidationUseCases
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