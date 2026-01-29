/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       EntityActionUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.usecase.entitites

import dev.bittim.valolink.user.data.keys.UserScopedKey
import dev.bittim.valolink.user.data.repository.synced.SyncedRepository
import dev.bittim.valolink.user.domain.usecase.auth.ResolveUidUseCase
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

class EntityActionUseCase @Inject constructor(
    private val resolveUid: ResolveUidUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun <T, K> insert(repository: SyncedRepository<T, K>, entity: T) {
        repository.insert(entity)
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun <T, K : UserScopedKey<K>> delete(repository: SyncedRepository<T, K>, key: K) {
        if (key.user != null) {
            repository.delete(key)
            return
        }

        val uid = resolveUid.current() ?: return
        repository.delete(key.withUser(uid))
    }

    @JvmName("deleteGlobal")
    suspend fun <T, K : Any> delete(repository: SyncedRepository<T, K>, key: K) {
        repository.delete(key)
    }
}