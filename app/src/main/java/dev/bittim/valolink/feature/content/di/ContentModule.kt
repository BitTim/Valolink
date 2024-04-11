package dev.bittim.valolink.feature.content.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.feature.content.data.local.GameDatabase
import dev.bittim.valolink.feature.content.data.remote.GameApi
import dev.bittim.valolink.feature.content.data.repository.ApiGameRepository
import dev.bittim.valolink.feature.content.data.repository.FirebaseUserRepository
import dev.bittim.valolink.feature.content.data.repository.GameRepository
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentModule {
    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth): UserRepository {
        return FirebaseUserRepository(auth)
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesGameDatabase(@ApplicationContext context: Context): GameDatabase {
        return Room.databaseBuilder(
            context,
            GameDatabase::class.java,
            "game.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesGameApi(): GameApi {
        return Retrofit.Builder()
            .baseUrl(GameApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GameApi::class.java)
    }

    @Provides
    @Singleton
    fun providesGameRepository(gameDatabase: GameDatabase, gameApi: GameApi): GameRepository {
        return ApiGameRepository(gameDatabase, gameApi)
    }
}