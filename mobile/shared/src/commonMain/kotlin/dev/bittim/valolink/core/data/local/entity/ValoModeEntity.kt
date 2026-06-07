/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 16:23
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import dev.bittim.valolink.core.data.util.localized
import dev.bittim.valolink.core.domain.model.ValoMode
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_modes",
    primaryKeys = ["uuid"]
)
data class ValoModeEntity(
    val uuid: Uuid,
    val displayName: Map<String, String>,
    val description: Map<String, String>?,
    val duration: Map<String, String>?,
    val category: String,
    val displayIcon: String?,
    val listViewIconTall: String?,
    val roundsPerHalf: Int,
    val canBeRanked: Boolean
) {
    fun toModel(locale: String?): ValoMode {
        return ValoMode(
            uuid = uuid,
            displayName = displayName.localized(locale),
            description = description?.localized(locale),
            duration = duration?.localized(locale),
            category = category,
            displayIcon = displayIcon,
            listViewIconTall = listViewIconTall,
            roundsPerHalf = roundsPerHalf,
            canBeRanked = canBeRanked
        )
    }
}