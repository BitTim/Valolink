/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           AppModule.kt
Author:         Tim Anhalt (BitTim)
Created:        25.03.2024
Description:    Handles dependency injection for the app.
*/

package dev.bittim.valolink.core.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.core.data.local.converter.StringListConverter
import dev.bittim.valolink.core.data.local.game.GameDatabase
import dev.bittim.valolink.core.data.remote.game.GameApi
import dev.bittim.valolink.core.data.repository.game.AgentApiRepository
import dev.bittim.valolink.core.data.repository.game.AgentRepository
import dev.bittim.valolink.core.data.repository.game.BuddyApiRepository
import dev.bittim.valolink.core.data.repository.game.BuddyRepository
import dev.bittim.valolink.core.data.repository.game.ContractApiRepository
import dev.bittim.valolink.core.data.repository.game.ContractRepository
import dev.bittim.valolink.core.data.repository.game.CurrencyApiRepository
import dev.bittim.valolink.core.data.repository.game.CurrencyRepository
import dev.bittim.valolink.core.data.repository.game.EventApiRepository
import dev.bittim.valolink.core.data.repository.game.EventRepository
import dev.bittim.valolink.core.data.repository.game.PlayerCardApiRepository
import dev.bittim.valolink.core.data.repository.game.PlayerCardRepository
import dev.bittim.valolink.core.data.repository.game.PlayerTitleApiRepository
import dev.bittim.valolink.core.data.repository.game.PlayerTitleRepository
import dev.bittim.valolink.core.data.repository.game.SeasonApiRepository
import dev.bittim.valolink.core.data.repository.game.SeasonRepository
import dev.bittim.valolink.core.data.repository.game.SprayApiRepository
import dev.bittim.valolink.core.data.repository.game.SprayRepository
import dev.bittim.valolink.core.data.repository.game.VersionApiRepository
import dev.bittim.valolink.core.data.repository.game.VersionRepository
import dev.bittim.valolink.core.data.repository.game.WeaponApiRepository
import dev.bittim.valolink.core.data.repository.game.WeaponRepository
import dev.bittim.valolink.core.domain.usecase.supabase.CreateSupabaseClientUseCase
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.domain.usecase.game.QueueFullSyncUseCase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	// ================================
	//  Supabase
	// ================================

	@Provides
	@Singleton
	fun provideSupabaseClient(createSupabaseClientUseCase: CreateSupabaseClientUseCase): SupabaseClient {
		return createSupabaseClientUseCase()
	}

	@Provides
	@Singleton
	fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
		return client.postgrest
	}

	@Provides
	@Singleton
	fun provideSupabaseAuth(client: SupabaseClient): Auth {
		return client.auth
	}

	@Provides
	@Singleton
	fun provideSupabaseRealtime(client: SupabaseClient): Realtime {
		return client.realtime
	}

	@Provides
	@Singleton
	fun provideSupabaseFunctions(client: SupabaseClient): Functions {
		return client.functions
	}

	@Provides
	@Singleton
	fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
		return WorkManager.getInstance(context)
	}

	// --------------------------------
	//  Database
	// --------------------------------

	@Provides
	@Singleton
	fun provideMoshi(): Moshi {
		return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
	}

	@Provides
	@Singleton
	fun provideGameDatabase(
		@ApplicationContext context: Context,
		moshi: Moshi,
	): GameDatabase {
		return Room.databaseBuilder(
			context, GameDatabase::class.java, "game.db"
		).addTypeConverter(StringListConverter(moshi)).build()
	}

	@Provides
	@Singleton
	fun provideUserDatabase(
		@ApplicationContext context: Context,
	): UserDatabase {
		return Room.databaseBuilder(
			context, UserDatabase::class.java, "user.db"
		).build()
	}

	// --------------------------------
	//  Api
	// --------------------------------

	@Provides
	@Singleton
	fun providesCache(@ApplicationContext context: Context): Cache {
		val cacheSize: Long = 5 * 1024 * 1024 // 5 MB
		return Cache(
			context.cacheDir, cacheSize
		)
	}

	@Provides
	@Singleton
	fun providesOkHttpClient(cache: Cache): OkHttpClient {
		return OkHttpClient.Builder().cache(cache).addInterceptor { chain ->
			val request = chain.request()
			request.newBuilder().header(
				"Cache-Control", "public, max-age=" + 5
			) // API response cache valid for 5 seconds
				.build()
			chain.proceed(request)
		}.build()
	}

	@Provides
	@Singleton
	fun providesGameApi(
		moshi: Moshi,
		okHttpClient: OkHttpClient,
	): GameApi {
		return Retrofit.Builder().baseUrl(GameApi.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create(moshi)).client(okHttpClient).build()
			.create(GameApi::class.java)
	}

	// ================================
	//  UseCases
	// ================================

	@Provides
	@Singleton
	fun provideCreateSupabaseClientUseCase(): CreateSupabaseClientUseCase {
		return CreateSupabaseClientUseCase()
	}

	// ================================
	// Repositories
	// ================================

	@Provides
	@Singleton
	fun providesVersionRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
	): VersionRepository {
		return VersionApiRepository(
			gameDatabase, gameApi
		)
	}

	@Provides
	@Singleton
	fun providesSeasonRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): SeasonRepository {
		return SeasonApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesEventRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): EventRepository {
		return EventApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesAgentRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): AgentRepository {
		return AgentApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesContractRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
		seasonRepository: SeasonRepository,
		eventRepository: EventRepository,
		agentRepository: AgentRepository,
		currencyRepository: CurrencyRepository,
		sprayRepository: SprayRepository,
		playerTitleRepository: PlayerTitleRepository,
		playerCardRepository: PlayerCardRepository,
		buddyRepository: BuddyRepository,
		weaponRepository: WeaponRepository,
	): ContractRepository {
		return ContractApiRepository(
			gameDatabase,
			gameApi,
			workManager,
			seasonRepository,
			eventRepository,
			agentRepository,
			currencyRepository,
			sprayRepository,
			playerTitleRepository,
			playerCardRepository,
			buddyRepository,
			weaponRepository,
		)
	}

	@Provides
	@Singleton
	fun providesCurrencyRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): CurrencyRepository {
		return CurrencyApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesSprayRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): SprayRepository {
		return SprayApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesPlayerTitleRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): PlayerTitleRepository {
		return PlayerTitleApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesPlayerCardRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): PlayerCardRepository {
		return PlayerCardApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesBuddyRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): BuddyRepository {
		return BuddyApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	@Provides
	@Singleton
	fun providesWeaponRepository(
		gameDatabase: GameDatabase,
		gameApi: GameApi,
		workManager: WorkManager,
	): WeaponRepository {
		return WeaponApiRepository(
			gameDatabase, gameApi, workManager
		)
	}

	// --------------------------------
	//  Use Cases
	// --------------------------------

	@Provides
	@Singleton
	fun provideQueueFullSyncUseCase(
		agentRepository: AgentRepository,
		buddyRepository: BuddyRepository,
		contractRepository: ContractRepository,
		currencyRepository: CurrencyRepository,
		eventRepository: EventRepository,
		playerCardRepository: PlayerCardRepository,
		playerTitleRepository: PlayerTitleRepository,
		seasonRepository: SeasonRepository,
		sprayRepository: SprayRepository,
		weaponRepository: WeaponRepository,
	): QueueFullSyncUseCase {
		return QueueFullSyncUseCase(
			agentRepository,
			buddyRepository,
			contractRepository,
			currencyRepository,
			eventRepository,
			playerCardRepository,
			playerTitleRepository,
			seasonRepository,
			sprayRepository,
			weaponRepository
		)
	}
}