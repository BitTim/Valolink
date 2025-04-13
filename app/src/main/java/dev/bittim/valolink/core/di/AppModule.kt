/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AppModule.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 14:44
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
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.remote.ContentApi
import dev.bittim.valolink.content.data.repository.agent.AgentApiRepository
import dev.bittim.valolink.content.data.repository.agent.AgentRepository
import dev.bittim.valolink.content.data.repository.buddy.BuddyApiRepository
import dev.bittim.valolink.content.data.repository.buddy.BuddyRepository
import dev.bittim.valolink.content.data.repository.contract.ContractApiRepository
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyApiRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyRepository
import dev.bittim.valolink.content.data.repository.event.EventApiRepository
import dev.bittim.valolink.content.data.repository.event.EventRepository
import dev.bittim.valolink.content.data.repository.playerCard.PlayerCardApiRepository
import dev.bittim.valolink.content.data.repository.playerCard.PlayerCardRepository
import dev.bittim.valolink.content.data.repository.playerTitle.PlayerTitleApiRepository
import dev.bittim.valolink.content.data.repository.playerTitle.PlayerTitleRepository
import dev.bittim.valolink.content.data.repository.season.SeasonApiRepository
import dev.bittim.valolink.content.data.repository.season.SeasonRepository
import dev.bittim.valolink.content.data.repository.spray.SprayApiRepository
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.content.data.repository.version.VersionApiRepository
import dev.bittim.valolink.content.data.repository.version.VersionRepository
import dev.bittim.valolink.content.data.repository.weapon.WeaponApiRepository
import dev.bittim.valolink.content.data.repository.weapon.WeaponRepository
import dev.bittim.valolink.content.domain.usecase.QueueFullSyncUseCase
import dev.bittim.valolink.core.data.local.converter.StringListConverter
import dev.bittim.valolink.core.domain.usecase.supabase.CreateSupabaseClientUseCase
import dev.bittim.valolink.user.data.local.UserDatabase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
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
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
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
    ): ContentDatabase {
        return Room.databaseBuilder(
            context, ContentDatabase::class.java, "game.db"
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
    ): ContentApi {
        return Retrofit.Builder().baseUrl(ContentApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).client(okHttpClient).build()
            .create(ContentApi::class.java)
    }

    // ================================
    // Repositories
    // ================================

    @Provides
    @Singleton
    fun providesVersionRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
    ): VersionRepository {
        return VersionApiRepository(
            contentDatabase, contentApi
        )
    }

    @Provides
    @Singleton
    fun providesSeasonRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): SeasonRepository {
        return SeasonApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesEventRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): EventRepository {
        return EventApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesAgentRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): AgentRepository {
        return AgentApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesContractRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
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
            contentDatabase,
            contentApi,
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
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): CurrencyRepository {
        return CurrencyApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesSprayRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): SprayRepository {
        return SprayApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesPlayerTitleRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): PlayerTitleRepository {
        return PlayerTitleApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesPlayerCardRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): PlayerCardRepository {
        return PlayerCardApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesBuddyRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): BuddyRepository {
        return BuddyApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    @Provides
    @Singleton
    fun providesWeaponRepository(
        contentDatabase: ContentDatabase,
        contentApi: ContentApi,
        workManager: WorkManager,
    ): WeaponRepository {
        return WeaponApiRepository(
            contentDatabase, contentApi, workManager
        )
    }

    // --------------------------------
    //  Use Cases
    // --------------------------------

    @Provides
    @Singleton
    fun provideCreateSupabaseClientUseCase(): CreateSupabaseClientUseCase {
        return CreateSupabaseClientUseCase()
    }

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
