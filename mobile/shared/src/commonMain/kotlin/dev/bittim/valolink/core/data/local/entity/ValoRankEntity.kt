/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 02:38
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.data.util.localized
import dev.bittim.valolink.core.domain.model.ValoRank
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_ranks",
    primaryKeys = ["rankTable", "tier"],
    foreignKeys = [
        ForeignKey(
            entity = ValoRankTableEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["rankTable"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoRankEntity(
    val rankTable: Uuid,
    val tier: Int,
    val tierName: Map<String, String>,
    val smallIcon: String?,
    val largeIcon: String?,
    val rankTriangleDownIcon: String?,
    val rankTriangleUpIcon: String?,
    val color: String,
    val backgroundColor: String,
    val division: String,
    val divisionName: Map<String, String>
) {
    /**
     * Converts this entity to a domain model with localized tier and division names.
     *
     * @param locale The locale code for localizing names. If null, a default locale is used.
     * @return A ValoRank domain model with localized names.
     */
    fun toModel(locale: String?): ValoRank {
        return ValoRank(
            rankTable = rankTable,
            tier = tier,
            tierName = tierName.localized(locale),
            smallIcon = smallIcon,
            largeIcon = largeIcon,
            rankTriangleDownIcon = rankTriangleDownIcon,
            rankTriangleUpIcon = rankTriangleUpIcon,
            color = color,
            backgroundColor = backgroundColor,
            division = division,
            divisionName = divisionName.localized(locale)
        )
    }
}