/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       UserEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "users",
    primaryKeys = ["id"]
)
data class UserEntity(
    val id: Uuid,
    val createdAt: Instant,
    val updatedAt: Instant,
    val username: String,
    val avatar: String?,
    val isPrivate: Boolean
)