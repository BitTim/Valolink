/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserAgent.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.model

import com.powersync.db.SqlCursor
import com.powersync.db.getString
import org.kotlincrypto.hash.sha2.SHA256
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class UserAgent @OptIn(ExperimentalUuidApi::class) constructor(
    override val createdAt: Instant,
    val user: Uuid,
    val agent: Uuid,
) : SyncedData {
    @OptIn(ExperimentalUuidApi::class)
    override fun generateId(): String {
        val rawId = user.toString() + agent.toString()
        return SHA256().digest(rawId.toByteArray()).toString()
    }

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun from(cursor: SqlCursor): UserAgent {
            return UserAgent(
                createdAt = Instant.parse(cursor.getString("created_at")),
                user = Uuid.parse(cursor.getString("user")),
                agent = Uuid.parse(cursor.getString("agent"))
            )
        }
    }
}
