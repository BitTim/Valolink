/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SessionSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:30
 */

package dev.bittim.valolink.user.data.repository

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import dev.bittim.valolink.user.data.flags.UserFlags
import dev.bittim.valolink.user.data.local.UserDatabase
import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import dev.bittim.valolink.user.domain.model.UserData
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SessionSupabaseRepository @Inject constructor(
    context: Context,
    private val auth: Auth,
    private val storage: Storage,
    private val userFlags: UserFlags,
    private val userDatabase: UserDatabase,
) : SessionRepository, DefaultLifecycleObserver {
    private val localAvatarPath: File = context.filesDir.resolve(File("avatar.jpg"))

    // ================================
    //  Session
    // ================================

    override fun getAuthenticated(): Flow<Boolean> {
        return userFlags.getLocal().combine(auth.sessionStatus) { isLocal, sessionStatus ->
            if (isLocal) {
                true
            } else {
                sessionStatus is SessionStatus.Authenticated
            }
        }
    }

    override suspend fun signOut() {
        // Reset local flag
        userFlags.setLocal(false)

        // Delete all cached user data on sign out
        userDatabase.userDataDao.deleteAll()
        userDatabase.userContractDao.deleteAll()

        // Delete local avatar if present
        if (localAvatarPath.exists()) {
            localAvatarPath.delete()
        }

        // Sign out
        auth.signOut()
    }


    override suspend fun isLocal(): Flow<Boolean> {
        return userFlags.getLocal()
    }

    override suspend fun createLocalUser() {
        userFlags.setLocal(true)
        createUser()
    }

    override suspend fun createUser() {
        userDatabase.userDataDao.upsert(
            UserDataEntity.fromType(
                userData = UserData(
                    uuid = getUid() ?: return,
                    isPrivate = true,
                    username = "",
                    onboardingStep = 0,
                    avatar = null,
                    agents = emptyList(),
                    contracts = emptyList()
                ),
                isSynced = false,
                toDelete = false
            )
        )
    }

    // ================================
    //  User Metadata
    // ================================

    override suspend fun getUserInfo(): UserInfo? {
        if (isLocal().first()) return null
        return auth.currentUserOrNull()
    }

    override suspend fun getUid(): String? {
        if (isLocal().first()) return UserDatabase.LOCAL_UUID
        return getUserInfo()?.id
    }

    override suspend fun getUserData(): Flow<UserDataEntity?> {
        return userDatabase.userDataDao.getByUuid(getUid() ?: return flow { })
    }

    override suspend fun getUsernameFromMetadata(): String? {
        if (isLocal().first()) return null
        return getUserInfo()?.userMetadata?.get("display_name")?.jsonPrimitive?.contentOrNull
    }


    override suspend fun getOnboardingStep(): Flow<Int?> {
        return getUserData().map { data -> data?.onboardingStep }
    }

    override suspend fun getUsername(): Flow<String?> {
        return getUserData().map { data -> data?.username }
    }

    override suspend fun getPrivate(): Flow<Boolean?> {
        return getUserData().map { data -> data?.isPrivate }
    }

    override suspend fun getAvatar(): Flow<String?> {
        return getUserData().map { data -> data?.avatar }
    }


    override suspend fun setOnboardingStep(onboardingStep: Int) {
        val userData = getUserData().first() ?: return
        userDatabase.userDataDao.upsert(userData.copy(onboardingStep = onboardingStep))
    }

    override suspend fun setUsername(username: String) {
        val userData = getUserData().first() ?: return
        userDatabase.userDataDao.upsert(userData.copy(username = username))
    }

    override suspend fun setPrivate(value: Boolean) {
        val userData = getUserData().first() ?: return
        userDatabase.userDataDao.upsert(userData.copy(isPrivate = value))
    }

    override suspend fun setAvatar(avatar: ByteArray) {
        val userData = getUserData().first() ?: return

        val location = if (isLocal().first()) {
            val filename = localAvatarPath
            val file = FileOutputStream(filename)
            file.write(avatar)
            file.close()
            filename.toString()
        } else {
            val bucket = storage.from("avatars")
            val filename = getUid() + ".jpg"
            bucket.update(filename, avatar) { upsert = true }
            filename
        }

        userDatabase.userDataDao.upsert(userData.copy(avatar = location))
    }
}
