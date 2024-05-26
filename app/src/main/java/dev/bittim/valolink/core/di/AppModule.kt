package dev.bittim.valolink.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesAppwriteClient(@ApplicationContext context: Context): Client {
        return Client(context).setEndpoint("https://cloud.appwrite.io/v1")
            .setProject("valolink")
    }

    @Provides
    @Singleton
    fun providesAppwriteAccount(client: Client): Account {
        return Account(client)
    }

    @Provides
    @Singleton
    fun providesAppwriteDatabases(client: Client): Databases {
        return Databases(client)
    }

    @Provides
    @Singleton
    fun providesAppwriteRealtime(client: Client): Realtime {
        return Realtime(client)
    }
}