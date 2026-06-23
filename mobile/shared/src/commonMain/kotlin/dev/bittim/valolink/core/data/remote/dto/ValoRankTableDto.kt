/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankTableDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:05
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.data.local.entity.ValoRankTableEntity
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ValoRankTableDto(
    val uuid: Uuid
) {
    /**
     * Converts this DTO to a domain entity.
     *
     * @return The corresponding ValoRankTableEntity.
     */
    fun toEntity(): ValoRankTableEntity {
        return ValoRankTableEntity(
            uuid = uuid,
        )
    }

}
