package dev.bittim.valolink.main.data.local.user.entity

abstract class SyncedEntity {
    abstract val isSynced: Boolean
    abstract val updatedAt: String
}