/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContract.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.model

import com.powersync.db.SqlCursor
import com.powersync.db.getBoolean
import com.powersync.db.getLong
import com.powersync.db.getString
import org.kotlincrypto.hash.sha2.SHA256
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class UserContract @OptIn(ExperimentalUuidApi::class) constructor(
    override val createdAt: Instant,
    val user: Uuid,
    val contract: Uuid,
    val freeOnly: Boolean,
    val xpOffset: Int,
) : SyncedData {
    @OptIn(ExperimentalUuidApi::class)
    override fun generateId(): String {
        val rawId = user.toString() + contract.toString()
        return SHA256().digest(rawId.toByteArray()).toString()
    }

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun from(cursor: SqlCursor): UserContract {
            return UserContract(
                createdAt = Instant.parse(cursor.getString("created_at")),
                user = Uuid.parse(cursor.getString("user")),
                contract = Uuid.parse(cursor.getString("contract")),
                freeOnly = cursor.getBoolean("free_only"),
                xpOffset = cursor.getLong("xp_offset").toInt(),
            )
        }
    }
}
