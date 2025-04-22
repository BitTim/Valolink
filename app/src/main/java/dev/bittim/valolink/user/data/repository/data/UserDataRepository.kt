/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDataRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 20:39
 */

package dev.bittim.valolink.user.data.repository.data

import dev.bittim.valolink.user.data.remote.dto.UserDataDto
import dev.bittim.valolink.user.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository : UserRepository<UserData, UserDataDto> {
    // ================================
    //  Get User Data
    // ================================

    fun getWithCurrentUser(): Flow<UserData?>
    fun get(uid: String): Flow<UserData?>

    fun hasOnboardedWithCurrentUser(): Flow<Boolean?>
    fun hasOnboarded(uid: String): Flow<Boolean?>

    suspend fun downloadAvatarWithCurrentUser(): ByteArray?
    suspend fun downloadAvatar(uid: String): ByteArray?

    // ================================
    //  Set User Data
    // ================================

    suspend fun createEmptyForCurrentUser(): Boolean
    suspend fun setWithCurrentUser(userData: UserData, toDelete: Boolean): Boolean
    suspend fun set(uid: String, userData: UserData, toDelete: Boolean): Boolean

    suspend fun setWithCurrentUser(userData: UserData): Boolean
    suspend fun set(uid: String, userData: UserData): Boolean

    suspend fun deleteWithCurrentUser(userData: UserData): Boolean
    suspend fun delete(uid: String, userData: UserData): Boolean

    suspend fun uploadAvatarWithCurrentUser(avatar: ByteArray): String?
    suspend fun uploadAvatar(uid: String, avatar: ByteArray): String?
}
