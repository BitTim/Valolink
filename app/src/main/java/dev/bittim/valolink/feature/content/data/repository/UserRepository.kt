package dev.bittim.valolink.feature.content.data.repository

interface UserRepository {
    fun hasUser(): Boolean
    fun getUsername(): String?
    fun signOut()
}