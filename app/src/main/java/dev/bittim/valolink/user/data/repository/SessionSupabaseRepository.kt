/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SessionSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:11
 */

package dev.bittim.valolink.user.data.repository

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import dev.bittim.valolink.user.data.flags.UserFlags
import dev.bittim.valolink.user.data.local.UserDatabase
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import javax.inject.Inject

class SessionSupabaseRepository @Inject constructor(
    private val context: Context,
    private val auth: Auth,
    private val userFlags: UserFlags,
    private val userDatabase: UserDatabase,
) : SessionRepository, DefaultLifecycleObserver {
    // ================================
    //  Session
    // ================================

    override fun isAuthenticated(): Flow<Boolean> {
        return userFlags.getLocal().combine(auth.sessionStatus) { isLocal, sessionStatus ->
            isLocal || sessionStatus is SessionStatus.Authenticated
        }
    }

    override suspend fun signOut() {
        // Reset local flag
        userFlags.setLocal(false)

        // Delete all cached user data on sign out
        userDatabase.userDataDao.deleteAll()
        userDatabase.userAgentDao.deleteAll()
        userDatabase.userContractDao.deleteAll()
        userDatabase.userLevelDao.deleteAll()

        // Delete local avatar if present
        val localAvatarPath =
            context.filesDir.resolve(File(SessionRepository.LOCAL_AVATAR_FILENAME))
        if (localAvatarPath.exists()) {
            localAvatarPath.delete()
        }

        // Sign out
        auth.signOut()
    }


    override fun isLocal(): Flow<Boolean> {
        return userFlags.getLocal()
    }

    override suspend fun setLocal(local: Boolean) {
        userFlags.setLocal(local)
    }

    // ================================
    //  User Metadata
    // ================================

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserInfo(): Flow<UserInfo?> {
        return isLocal().flatMapLatest { isLocal ->
            if (isLocal) flowOf(null) else auth.sessionStatus.map { status -> if (status is SessionStatus.Authenticated) status.session.user else null }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUid(): Flow<String?> {
        return isLocal().flatMapLatest { isLocal ->
            if (isLocal) flowOf(UserDatabase.LOCAL_UUID) else getUserInfo().map { it?.id }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUsernameFromMetadata(): Flow<String?> {
        return isLocal().flatMapLatest { isLocal ->
            if (isLocal) flow { emit(null) } else getUserInfo().map {
                it?.userMetadata?.get(
                    "display_name"
                )?.jsonPrimitive?.contentOrNull
            }
        }
    }
}
