package dev.bittim.valolink.feature.content.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun hasUser(): Flow<Boolean>
    fun getUsername(): String?
    fun signOut()
}