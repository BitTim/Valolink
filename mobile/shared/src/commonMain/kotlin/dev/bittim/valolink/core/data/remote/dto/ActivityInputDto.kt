/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityInputDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:31
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.ActivityDraft
import dev.bittim.valolink.core.domain.model.ActivityType
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ActivityInputDto(
    val time: Instant,
    val type: ActivityType,
    val xp: Int,
    val rr: Int?,
    val mode: Uuid?
) {
    companion object {
        fun fromModel(activityDraft: ActivityDraft): ActivityInputDto {
            return ActivityInputDto(
                time = activityDraft.time,
                type = activityDraft.type,
                xp = activityDraft.xp,
                rr = activityDraft.rr,
                mode = activityDraft.mode
            )
        }

    }
}
