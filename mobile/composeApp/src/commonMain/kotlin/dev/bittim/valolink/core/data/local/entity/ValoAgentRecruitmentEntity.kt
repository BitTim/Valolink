/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoAgentRecruitmentEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:42
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_agent_recruitments",
    primaryKeys = ["agent"],
    foreignKeys = [
        ForeignKey(
            entity = ValoAgentEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["agent"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoAgentRecruitmentEntity(
    val agent: Uuid,
    val xp: Int,
    val startTime: Instant,
    val endTime: Instant
)