/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 20:21
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.data.local.embedded.ValoMapCallout
import dev.bittim.valolink.core.data.local.entity.ValoMapEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ValoMapDto(
    val uuid: Uuid,
    @SerialName("display_name") val displayName: Map<String, String>,
    @SerialName("tactical_description") val tacticalDescription: Map<String, String>?,
    val coordinates: Map<String, String>?,
    val category: String,
    @SerialName("list_view_icon") val listViewIcon: String,
    @SerialName("list_view_icon_tall") val listViewIconTall: String,
    val splash: String,
    @SerialName("premier_background_image") val premierBackgroundImage: String?,
    @SerialName("stylized_background_image") val stylizedBackgroundImage: String?,
    @SerialName("display_icon") val displayIcon: String?,
    @SerialName("x_multiplier") val xMultiplier: Float,
    @SerialName("x_scalar_to_add") val xScalarToAdd: Float,
    @SerialName("y_multiplier") val yMultiplier: Float,
    @SerialName("y_scalar_to_add") val yScalarToAdd: Float,
    val callouts: List<ValoMapCallout>?
) {
    fun toEntity(): ValoMapEntity {
        return ValoMapEntity(
            uuid = uuid,
            displayName = displayName,
            tacticalDescription = tacticalDescription,
            coordinates = coordinates,
            category = category,
            listViewIcon = listViewIcon,
            listViewIconTall = listViewIconTall,
            splash = splash,
            premierBackgroundImage = premierBackgroundImage,
            stylizedBackgroundImage = stylizedBackgroundImage,
            displayIcon = displayIcon,
            xMultiplier = xMultiplier,
            xScalarToAdd = xScalarToAdd,
            yMultiplier = yMultiplier,
            yScalarToAdd = yScalarToAdd,
            callouts = callouts
        )
    }

}

