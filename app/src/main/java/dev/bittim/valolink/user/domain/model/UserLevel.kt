/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.model

import com.powersync.db.SqlCursor
import com.powersync.db.getBoolean
import com.powersync.db.getString
import org.kotlincrypto.hash.sha2.SHA256
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class UserLevel @OptIn(ExperimentalUuidApi::class) constructor(
    override val createdAt: Instant,
    val contract: Uuid,
    val level: Uuid,
    val isPurchased: Boolean,
    val user: Uuid,
) : SyncedData {
    @OptIn(ExperimentalUuidApi::class)
    override fun generateId(): String {
        val rawId = user.toString() + contract.toString() + level.toString()
        return SHA256().digest(rawId.toByteArray()).toString()
    }

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun from(cursor: SqlCursor): UserLevel {
            return UserLevel(
                createdAt = Instant.parse(cursor.getString("created_at")),
                contract = Uuid.parse(cursor.getString("contract")),
                level = Uuid.parse(cursor.getString("level")),
                isPurchased = cursor.getBoolean("is_purchased"),
                user = Uuid.parse(cursor.getString("user"))
            )
        }
    }
}
