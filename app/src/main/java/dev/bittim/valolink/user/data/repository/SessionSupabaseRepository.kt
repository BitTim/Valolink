/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SessionSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:07
 */

package dev.bittim.valolink.user.data.repository

import androidx.lifecycle.DefaultLifecycleObserver
import dev.bittim.valolink.user.data.flags.UserFlags
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class SessionSupabaseRepository @Inject constructor(
    private val auth: Auth,
    private val storage: Storage,
    private val userFlags: UserFlags,
    private val userDatabase: UserDatabase,
) : SessionRepository, DefaultLifecycleObserver {
    // ================================
    //  Session
    // ================================

    override fun getAuthenticated(): Flow<Boolean> {
        val localFlow = userFlags.getLocal
        auth.sessionStatus

        return localFlow.combine(auth.sessionStatus) { isLocal, sessionStatus ->
            if (isLocal) {
                return@combine true
            } else {
                return@combine sessionStatus is SessionStatus.Authenticated
            }
        }
    }

    override suspend fun signOut() {
        // Delete all cached user data on sign out
        userDatabase.userDataDao.deleteAll()
        userDatabase.userContractDao.deleteAll()

        // Sign out
        auth.signOut()
    }

    // ================================
    //  User Metadata
    // ================================

    override suspend fun getUserInfo(): UserInfo? {
        return auth.currentUserOrNull()
    }

    override suspend fun getUid(): String? {
        return getUserInfo()?.id
    }

    override suspend fun getUserData(): UserDataEntity? {
        return userDatabase.userDataDao.getByUuid(getUid() ?: return null).first()
    }

    override suspend fun getUsernameFromMetadata(): String? {
        return getUserInfo()?.userMetadata?.get("display_name")?.jsonPrimitive?.contentOrNull
    }


    override suspend fun getOnboardingStep(): Int? {
        return getUserData()?.onboardingStep
    }

    override suspend fun getUsername(): String? {
        return getUserData()?.username
    }

    override suspend fun getPrivate(): Boolean? {
        return getUserData()?.isPrivate
    }

    override suspend fun getAvatar(): String? {
        return getUserData()?.avatar
    }


    override suspend fun setOnboardingStep(onboardingStep: Int) {
        val userData = getUserData() ?: return
        userDatabase.userDataDao.upsert(userData.copy(onboardingStep = onboardingStep))
    }

    override suspend fun setUsername(username: String) {
        val userData = getUserData() ?: return
        userDatabase.userDataDao.upsert(userData.copy(username = username))
    }

    override suspend fun setPrivate(value: Boolean) {
        val userData = getUserData() ?: return
        userDatabase.userDataDao.upsert(userData.copy(isPrivate = value))
    }

    override suspend fun setAvatar(avatar: ByteArray) {
        val userData = getUserData() ?: return

        val bucket = storage.from("avatars")
        val filename = getUid() + ".jpg"
        bucket.update(filename, avatar) { upsert = true }

        userDatabase.userDataDao.upsert(userData.copy(avatar = filename))
    }
}
