/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserAgentPowersyncRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.repository.synced

import com.powersync.PowerSyncDatabase
import dev.bittim.valolink.user.data.keys.UserAgentKey
import dev.bittim.valolink.user.domain.model.UserAgent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi

class UserAgentPowersyncRepository @Inject constructor(
    private val userDatabase: PowerSyncDatabase,
) : UserAgentRepository {
    // region:      -- Read
    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    override fun get(key: UserAgentKey): Flow<List<UserAgent>> {
        return userDatabase.watch(
            "SELECT * FROM agents WHERE user = ? AND agent = COALESCE(?, agent)",
            listOf(key.user, key.agent)
        ) { cursor -> UserAgent.from(cursor) }
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
    override suspend fun insert(obj: UserAgent): Long {
        return try {
            userDatabase.execute(
                "INSERT INTO agents (id, created_at, user, agent) VALUES (?, ?, ?, ?) ON CONFLICT (user, agent) DO UPDATE SET created_at = EXCLUDED.created_at",
                listOf(
                    obj.generateId(),
                    obj.createdAt.toString(),
                    obj.user.toString(),
                    obj.agent.toString()
                )
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(key: UserAgentKey): Long {
        return try {
            userDatabase.execute(
                "DELETE FROM agents WHERE user = ? AND agent = ?",
                listOf(key.user, key.agent)
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            0
        }
    }
    // endregion:   -- Write
}
