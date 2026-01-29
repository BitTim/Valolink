/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractPowersyncRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import com.powersync.PowerSyncDatabase
import dev.bittim.valolink.user.data.keys.UserContractKey
import dev.bittim.valolink.user.domain.model.UserContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi

class UserContractPowersyncRepository @Inject constructor(
    private val userDatabase: PowerSyncDatabase,
) : UserContractRepository {
    // region:      -- Read
    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
    override fun get(key: UserContractKey): Flow<List<UserContract>> {
        return userDatabase.watch(
            "SELECT * FROM contracts WHERE user = ? AND contract = COALESCE(?, contract)",
            listOf(key.user, key.contract)
        ) { cursor -> UserContract.from(cursor) }
            .distinctUntilChanged()
            .catch { e ->
                if (e is CancellationException) throw e
                e.printStackTrace()
                emit(listOf())
            }
    }
    // endregion:   -- Read

    // region:      -- Write
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insert(obj: UserContract): Long {
        return try {
            userDatabase.execute(
                "INSERT INTO contracts (id, created_at, user, contract, free_only, xp_offset) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (user, contract) DO UPDATE SET created_at = EXCLUDED.created_at, free_only = EXCLUDED.free_only, xp_offset = EXCLUDED.xp_offset",
                listOf(
                    obj.generateId(),
                    obj.createdAt.toString(),
                    obj.user.toString(),
                    obj.contract.toString(),
                    obj.freeOnly,
                    obj.xpOffset
                )
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(key: UserContractKey): Long {
        return try {
            userDatabase.execute(
                "DELETE FROM contracts WHERE user = ? AND contract = ?",
                listOf(key.user, key.contract)
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }
    // endregion:   -- Write
}
