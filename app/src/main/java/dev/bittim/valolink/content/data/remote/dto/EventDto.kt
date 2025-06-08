/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       EventDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 16:03
 */

package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.EventEntity

data class EventDto(
    val uuid: String,
    val displayName: String?,
    val shortDisplayName: String?,
    val startTime: String,
    val endTime: String,
    val assetPath: String,
) {
    fun toEntity(version: String): EventEntity {
        return EventEntity(
            uuid, version, displayName, shortDisplayName, startTime, endTime, assetPath
        )
    }
}
