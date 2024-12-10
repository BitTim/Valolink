package dev.bittim.valolink.user.data.local.entity

interface SyncedEntity {
    val uuid: String
    val isSynced: Boolean
    val toDelete: Boolean
    val updatedAt: String

    fun getIdentifier(): String
    fun withIsSynced(isSynced: Boolean): SyncedEntity
}