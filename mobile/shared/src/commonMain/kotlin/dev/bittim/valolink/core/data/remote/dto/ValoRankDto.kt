/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:17
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.data.local.entity.ValoRankEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ValoRankDto(
    @SerialName("rank_table") val rankTable: Uuid,
    val tier: Int,
    @SerialName("tier_name") val tierName: Map<String, String>,
    @SerialName("small_icon") val smallIcon: String?,
    @SerialName("large_icon") val largeIcon: String?,
    @SerialName("rank_triangle_down_icon") val rankTriangleDownIcon: String?,
    @SerialName("rank_triangle_up_icon") val rankTriangleUpIcon: String?,
    val color: String,
    @SerialName("background_color") val backgroundColor: String,
    val division: String,
    @SerialName("division_name") val divisionName: Map<String, String>,
) {
    /**
     * Converts this DTO to a ValoRankEntity.
     *
     * @return The corresponding entity representation.
     */
    fun toEntity(): ValoRankEntity {
        return ValoRankEntity(
            rankTable = rankTable,
            tier = tier,
            tierName = tierName,
            smallIcon = smallIcon,
            largeIcon = largeIcon,
            rankTriangleDownIcon = rankTriangleDownIcon,
            rankTriangleUpIcon = rankTriangleUpIcon,
            color = color,
            backgroundColor = backgroundColor,
            division = division,
            divisionName = divisionName
        )
    }
    
}
