/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 19:56
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import dev.bittim.valolink.core.data.local.embedded.ValoMapCallout
import dev.bittim.valolink.core.data.util.localized
import dev.bittim.valolink.core.domain.model.SimpleValoMap
import dev.bittim.valolink.core.domain.model.ValoMapCategory
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_maps",
    primaryKeys = ["uuid"]
)
data class ValoMapEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val tacticalDescription: Map<String, String>?,
    val coordinates: Map<String, String>?,
    val category: String,
    val listViewIcon: String,
    val listViewIconTall: String,
    val splash: String,
    val premierBackgroundImage: String?,
    val stylizedBackgroundImage: String?,
    val displayIcon: String?,
    val xMultiplier: Float,
    val xScalarToAdd: Float,
    val yMultiplier: Float,
    val yScalarToAdd: Float,
    val callouts: List<ValoMapCallout>?
) {
    fun toSimpleModel(locale: String?): SimpleValoMap {
        return SimpleValoMap(
            uuid = uuid,
            displayName = displayName.localized(locale),
            coordinates = coordinates?.localized(locale),
            category = ValoMapCategory.parse(category),
            listViewIcon = listViewIcon,
            listViewIconTall = listViewIconTall,
            splash = splash,
            premierBackgroundImage = premierBackgroundImage,
            stylizedBackgroundImage = stylizedBackgroundImage
        )
    }
}