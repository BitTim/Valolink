/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 19:10
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ActivityType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ActivityDto(
    val id: Uuid,
    @SerialName("user_id") val userId: Uuid,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    val time: Instant,
    val type: ActivityType,
    val xp: Int,
    val rr: Int?,
    val mode: Uuid?
) {
    /**
     * Converts this DTO into an activity domain model.
     *
     * @return The corresponding activity model.
     */
    fun toModel(): Activity {
        return Activity(
            id = id,
            userId = userId,
            time = time,
            type = type,
            xp = xp,
            rr = rr,
            mode = mode
        )
    }

}
