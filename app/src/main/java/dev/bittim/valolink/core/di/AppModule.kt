package dev.bittim.valolink.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.bittim.valolink.BuildConfig
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            BuildConfig.SUPABASE_URL,
            BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                flowType = FlowType.PKCE
            }
            install(Realtime)
            install(Functions)
        }
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
    fun provideSupabaseStorage(client: SupabaseClient): Functions {
        return client.functions
    }

    @Provides
    @Singleton
    fun providesAppwriteClient(@ApplicationContext context: Context): Client {
        return Client(context).setEndpoint("https://cloud.appwrite.io/v1").setProject("valolink")
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
    fun providesAppwriteRealtime(client: Client): io.appwrite.services.Realtime {
        return io.appwrite.services.Realtime(client)
    }
}