/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       WatchEntitiesUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.usecase.entitites

import dev.bittim.valolink.user.data.keys.UserScopedKey
import dev.bittim.valolink.user.data.repository.synced.SyncedRepository
import dev.bittim.valolink.user.domain.usecase.auth.ResolveUidUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

class WatchEntitiesUseCase @Inject constructor(
    private val resolveUid: ResolveUidUseCase
) {
    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    operator fun <T, K : UserScopedKey<K>> invoke(
        repository: SyncedRepository<T, K>,
        key: K,
    ): Flow<List<T>?> {
        if (key.user != null) {
            return repository.get(key)
        }

        return resolveUid.watch().flatMapLatest { uid ->
            if (uid == null) flowOf(null)
            else repository.get(key.withUser(uid))
        }
    }

    @JvmName("invokeGlobal")
    operator fun <T, K : Any> invoke(
        repository: SyncedRepository<T, K>,
        key: K,
    ): Flow<List<T>?> {
        return repository.get(key)
    }
}