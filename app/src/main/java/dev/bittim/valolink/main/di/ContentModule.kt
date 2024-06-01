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
import dev.bittim.valolink.main.data.repository.game.BuddyLevelApiRepository
import dev.bittim.valolink.main.data.repository.game.BuddyLevelRepository
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
import dev.bittim.valolink.main.data.repository.game.WeaponSkinLevelApiRepository
import dev.bittim.valolink.main.data.repository.game.WeaponSkinLevelRepository
import dev.bittim.valolink.main.data.repository.user.SupabaseUserRepository
import dev.bittim.valolink.main.data.repository.user.UserRepository
import io.github.jan.supabase.gotrue.Auth
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentModule {
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
        moshi: Moshi,
    ): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user.db"
        ).addTypeConverter(StringListConverter(moshi)).build()
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
    fun provideUserRepository(
        auth: Auth,
        userDatabase: UserDatabase,
        workManager: WorkManager,
    ): UserRepository {
        return SupabaseUserRepository(
            auth,
            userDatabase,
            workManager
        )
    }

    @Provides
    @Singleton
    fun providesVersionRepository(gameApi: GameApi): VersionRepository {
        return VersionApiRepository(gameApi)
    }

    @Provides
    @Singleton
    fun providesSeasonRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): SeasonRepository {
        return SeasonApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesEventRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): EventRepository {
        return EventApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesAgentRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): AgentRepository {
        return AgentApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesContractRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
        seasonRepository: SeasonRepository,
        eventRepository: EventRepository,
        agentRepository: AgentRepository,
    ): ContractRepository {
        return ContractApiRepository(
            gameDatabase,
            gameApi,
            versionRepository,
            seasonRepository,
            eventRepository,
            agentRepository
        )
    }

    @Provides
    @Singleton
    fun providesCurrencyRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): CurrencyRepository {
        return CurrencyApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesSprayRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): SprayRepository {
        return SprayApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesPlayerTitleRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): PlayerTitleRepository {
        return PlayerTitleApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesPlayerCardRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): PlayerCardRepository {
        return PlayerCardApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesBuddyLevelRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): BuddyLevelRepository {
        return BuddyLevelApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }

    @Provides
    @Singleton
    fun providesWeaponSkinLevelRepository(
        gameDatabase: GameDatabase,
        gameApi: GameApi,
        versionRepository: VersionRepository,
    ): WeaponSkinLevelRepository {
        return WeaponSkinLevelApiRepository(
            gameDatabase,
            gameApi,
            versionRepository
        )
    }
}