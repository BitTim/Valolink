package dev.bittim.valolink.feature.content.data

interface UserRepository {
    fun hasUser(): Boolean
    fun getUsername(): String?
    fun signOut()
}