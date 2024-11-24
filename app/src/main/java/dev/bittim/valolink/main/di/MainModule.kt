package dev.bittim.valolink.main.di

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.repository.user.SessionRepository
import dev.bittim.valolink.main.data.repository.user.SessionSupabaseRepository
import dev.bittim.valolink.main.data.repository.user.data.UserAgentRepository
import dev.bittim.valolink.main.data.repository.user.data.UserAgentSupabaseRepository
import dev.bittim.valolink.main.data.repository.user.data.UserContractRepository
import dev.bittim.valolink.main.data.repository.user.data.UserContractSupabaseRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataSupabaseRepository
import dev.bittim.valolink.main.data.repository.user.data.UserLevelRepository
import dev.bittim.valolink.main.data.repository.user.data.UserLevelSupabaseRepository
import dev.bittim.valolink.main.data.repository.user.onboarding.OnboardingRepository
import dev.bittim.valolink.main.data.repository.user.onboarding.OnboardingSupabaseRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
	// --------------------------------
	//  Repositories
	// --------------------------------

	@Provides
	@Singleton
	fun provideSessionRepository(
		auth: Auth,
		userDatabase: UserDatabase,
	): SessionRepository {
		return SessionSupabaseRepository(
			auth, userDatabase
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

	@Provides
	@Singleton
	fun provideOnboardingRepository(
		sessionRepository: SessionRepository,
		userDataRepository: UserDataRepository,
	): OnboardingRepository {
		return OnboardingSupabaseRepository(
			sessionRepository, userDataRepository
		)
	}
}