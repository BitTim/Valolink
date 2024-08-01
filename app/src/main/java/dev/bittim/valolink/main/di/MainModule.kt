package dev.bittim.valolink.main.di

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
import dev.bittim.valolink.main.data.local.converter.StringListConverter
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.repository.game.AgentApiRepository
import dev.bittim.valolink.main.data.repository.game.AgentRepository
import dev.bittim.valolink.main.data.repository.game.BuddyApiRepository
import dev.bittim.valolink.main.data.repository.game.BuddyRepository
import dev.bittim.valolink.main.data.repository.game.ContractApiRepository
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyApiRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.game.EventApiRepository
import dev.bittim.valolink.main.data.repository.game.EventRepository
import dev.bittim.valolink.main.data.repository.game.PlayerCardApiRepository
import dev.bittim.valolink.main.data.repository.game.PlayerCardRepository
import dev.bittim.valolink.main.data.repository.game.PlayerTitleApiRepository
import dev.bittim.valolink.main.data.repository.game.PlayerTitleRepository
import dev.bittim.valolink.main.data.repository.game.SeasonApiRepository
import dev.bittim.valolink.main.data.repository.game.SeasonRepository
import dev.bittim.valolink.main.data.repository.game.SprayApiRepository
import dev.bittim.valolink.main.data.repository.game.SprayRepository
import dev.bittim.valolink.main.data.repository.game.VersionApiRepository
import dev.bittim.valolink.main.data.repository.game.VersionRepository
import dev.bittim.valolink.main.data.repository.game.WeaponApiRepository
import dev.bittim.valolink.main.data.repository.game.WeaponRepository
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
import dev.bittim.valolink.main.domain.usecase.game.QueueFullSyncUseCase
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
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
            context,
            GameDatabase::class.java,
            "game.db"
        ).addTypeConverter(StringListConverter(moshi)).build()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(
        @ApplicationContext context: Context,
    ): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user.db"
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
            context.cacheDir,
            cacheSize
        )
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().cache(cache).addInterceptor { chain ->
            val request = chain.request()
            request.newBuilder().header(
                "Cache-Control",
                "public, max-age=" + 5
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
        return Retrofit
            .Builder()
            .baseUrl(GameApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(GameApi::class.java)
    }

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
            auth,
            userDatabase
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
            userDatabase,
            database,
            workManager
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
            sessionRepository,
            userLevelRepository,
            userDatabase,
            database,
            workManager
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
            sessionRepository,
            userDatabase,
            database,
            workManager
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
            sessionRepository,
            userDataRepository
        )
    }

    @Provides
    @Singleton
    fun providesVersionRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
    ): VersionRepository {
        return VersionApiRepository(
            gameDatabase,
            gameApi
        )
    }

    @Provides
    @Singleton
    fun providesSeasonRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): SeasonRepository {
        return SeasonApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesEventRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): EventRepository {
        return EventApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesAgentRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): AgentRepository {
        return AgentApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
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
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): CurrencyRepository {
        return CurrencyApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesSprayRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): SprayRepository {
        return SprayApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesPlayerTitleRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): PlayerTitleRepository {
        return PlayerTitleApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesPlayerCardRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): PlayerCardRepository {
        return PlayerCardApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesBuddyRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        workManager: WorkManager,
    ): BuddyRepository {
        return BuddyApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            workManager
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
            gameDatabase,
            gameApi,
            workManager
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