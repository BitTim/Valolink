package dev.bittim.valolink.main.data.remote.user.dto

abstract class SyncedDto<T>() {
    abstract val uuid: String
    abstract val updatedAt: String
    abstract fun getIdentifier(): String

    abstract fun toEntity(isSynced: Boolean, toDelete: Boolean): T
}
