/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       User.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.domain.model

import com.powersync.db.SqlCursor
import com.powersync.db.getBoolean
import com.powersync.db.getString
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class User @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    override val createdAt: Instant,
    val username: String,
    val isPrivate: Boolean,
    val avatar: String?,
    val hasOnboarded: Boolean,
    val hasRank: Boolean,
) : SyncedData {
    @OptIn(ExperimentalUuidApi::class)
    override fun generateId(): String {
        return id.toString()
    }

    companion object {
//        fun empty(uid: String): UserMeta {
//            return UserMeta(
//                uuid = uid,
//                isPrivate = false,
//                username = "",
//                onboardingStep = 0,
//                avatar = null,
//                rank = null,
//            )
//        }

        @OptIn(ExperimentalUuidApi::class)
        fun from(cursor: SqlCursor): User {
            return User(
                id = Uuid.parse(cursor.getString("id")),
                createdAt = Instant.parse(cursor.getString("created_at")),
                username = cursor.getString("username"),
                isPrivate = cursor.getBoolean("is_private"),
                avatar = cursor.getString("avatar"),
                hasOnboarded = cursor.getBoolean("has_onboarded"),
                hasRank = cursor.getBoolean("has_rank")
            )
        }
    }
}
