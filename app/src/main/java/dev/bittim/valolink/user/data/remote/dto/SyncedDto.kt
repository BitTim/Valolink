package dev.bittim.valolink.user.data.remote.dto

abstract class SyncedDto<T> {
    abstract val uuid: String
    abstract val updatedAt: String
    abstract fun getIdentifier(): String

    abstract fun toEntity(isSynced: Boolean, toDelete: Boolean): T
}
