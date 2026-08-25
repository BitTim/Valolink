/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 16:57
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ActivityType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
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

    companion object {
        /**
         * Creates an activity data-transfer object from a domain model.
         *
         * @param activity The activity domain model to convert.
         * @return An activity data-transfer object with current creation and update timestamps.
         */
        fun fromModel(activity: Activity): ActivityDto {
            return ActivityDto(
                id = activity.id,
                userId = activity.userId,
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
                time = activity.time,
                type = activity.type,
                xp = activity.xp,
                rr = activity.rr,
                mode = activity.mode
            )
        }

    }
}
