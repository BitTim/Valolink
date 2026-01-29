/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserPowersyncRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import com.powersync.PowerSyncDatabase
import dev.bittim.valolink.user.data.keys.UserKey
import dev.bittim.valolink.user.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi

class UserPowersyncRepository @Inject constructor(
    private val userDatabase: PowerSyncDatabase
) : UserRepository {
    // region:      -- Read
    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    override fun get(key: UserKey): Flow<List<User>?> {
        return userDatabase.watch(
            "SELECT * FROM users WHERE id = ?",
            listOf(key.user)
        ) { cursor -> User.from(cursor) }
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
    override suspend fun insert(obj: User): Long {
        return try {
            userDatabase.execute(
                "INSERT INTO users (id, created_at, username, is_private, avatar, has_onboarded, has_rank) VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET created_at = EXCLUDED.created_at, username = EXCLUDED.username, is_private = EXCLUDED.is_private, avatar = EXCLUDED.avatar, has_onboarded = EXCLUDED.has_onboarded, has_rank = EXCLUDED.has_rank)",
                listOf(
                    obj.generateId(),
                    obj.createdAt.toString(),
                    obj.username,
                    obj.isPrivate,
                    obj.avatar,
                    obj.hasOnboarded,
                    obj.hasRank
                )
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(key: UserKey): Long {
        return try {
            userDatabase.execute(
                "DELETE FROM users WHERE id = ?",
                listOf(key.user)
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }
    // endregion:   -- Write
}
