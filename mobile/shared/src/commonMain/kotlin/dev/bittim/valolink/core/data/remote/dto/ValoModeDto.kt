/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 16:56
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.data.local.entity.ValoModeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ValoModeDto(
    val uuid: Uuid,
    @SerialName("display_name") val displayName: Map<String, String>,
    val description: Map<String, String>?,
    val duration: Map<String, String>?,
    val category: String,
    @SerialName("display_icon") val displayIcon: String?,
    @SerialName("list_view_icon_tall") val listViewIconTall: String?,
    @SerialName("rounds_per_half") val roundsPerHalf: Int,
    @SerialName("can_be_ranked") val canBeRanked: Boolean
) {
    fun toEntity(): ValoModeEntity {
        return ValoModeEntity(
            uuid = uuid,
            displayName = displayName,
            description = description,
            duration = duration,
            category = category,
            displayIcon = displayIcon,
            listViewIconTall = listViewIconTall,
            roundsPerHalf = roundsPerHalf,
            canBeRanked = canBeRanked
        )
    }
}
