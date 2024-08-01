package dev.bittim.valolink.main.data.repository.user.data

interface UserRepository<T, D> {
    suspend fun remoteQuery(relation: String): List<D>?
    suspend fun remoteUpsert(uuid: String): Boolean
    suspend fun remoteDelete(uuid: String): Boolean
}