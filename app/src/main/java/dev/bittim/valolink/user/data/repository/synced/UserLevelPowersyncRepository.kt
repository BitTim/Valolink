/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevelPowersyncRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import com.powersync.PowerSyncDatabase
import dev.bittim.valolink.user.data.keys.UserLevelKey
import dev.bittim.valolink.user.domain.model.UserLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi

class UserLevelPowersyncRepository @Inject constructor(
    private val userDatabase: PowerSyncDatabase,
) : UserLevelRepository {
    // region:      -- Read
    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    override fun get(key: UserLevelKey): Flow<List<UserLevel>> {
        return userDatabase.watch(
            "SELECT * FROM levels WHERE user = ? AND contract = ? AND level = COALESCE(?, level)",
            listOf(key.user, key.contract, key.level)
        ) { cursor -> UserLevel.from(cursor) }
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
    override suspend fun insert(obj: UserLevel): Long {
        return try {
            userDatabase.execute(
                "INSERT INTO levels (id, created_at, contract, level, is_purchased, user) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (contract, level, user) DO UPDATE SET created_at = EXCLUDED.created_at, is_purchased = EXCLUDED.is_purchased",
                listOf(
                    obj.generateId(),
                    obj.createdAt.toString(),
                    obj.contract.toString(),
                    obj.level.toString(),
                    obj.isPurchased,
                    obj.user.toString()
                )
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(key: UserLevelKey): Long {
        return try {
            userDatabase.execute(
                "DELETE FROM levels WHERE user = ? AND contract = ? AND level = ?",
                listOf(key.user, key.contract, key.level)
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }
    // endregion:   -- Write
}
