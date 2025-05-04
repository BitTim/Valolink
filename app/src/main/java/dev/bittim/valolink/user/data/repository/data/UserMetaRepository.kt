/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDataRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:54
 */

package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserMetaDto
import dev.bittim.valolink.user.domain.model.UserMeta
import kotlinx.coroutines.flow.Flow

interface UserMetaRepository : UserRepository<UserMeta, UserMetaDto> {
    // ================================
    //  Get User Data
    // ================================

    fun getWithCurrentUser(): Flow<UserMeta?>
    fun get(uid: String): Flow<UserMeta?>

    fun hasOnboardedWithCurrentUser(): Flow<Boolean?>
    fun hasOnboarded(uid: String): Flow<Boolean?>

    suspend fun downloadAvatarWithCurrentUser(): ByteArray?
    suspend fun downloadAvatar(uid: String): ByteArray?

    // ================================
    //  Set User Data
    // ================================

    suspend fun createEmptyForCurrentUser(): Boolean
    suspend fun setWithCurrentUser(userMeta: UserMeta, toDelete: Boolean): Boolean
    suspend fun set(uid: String, userMeta: UserMeta, toDelete: Boolean): Boolean

    suspend fun setWithCurrentUser(userMeta: UserMeta): Boolean
    suspend fun set(uid: String, userMeta: UserMeta): Boolean

    suspend fun deleteWithCurrentUser(userMeta: UserMeta): Boolean
    suspend fun delete(uid: String, userMeta: UserMeta): Boolean

    suspend fun uploadAvatarWithCurrentUser(avatar: ByteArray): String?
    suspend fun uploadAvatar(uid: String, avatar: ByteArray): String?
}
