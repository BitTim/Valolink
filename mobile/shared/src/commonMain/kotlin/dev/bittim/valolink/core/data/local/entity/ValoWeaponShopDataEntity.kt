/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponShopDataEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.data.local.embedded.GridPosition
import kotlin.uuid.Uuid

@Entity(
    tableName = "valo_weapon_shop_data",
    primaryKeys = ["weapon"],
    foreignKeys = [
        ForeignKey(
            entity = ValoWeaponEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["weapon"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ValoWeaponShopDataEntity(
    val weapon: Uuid,
    val cost: Int,
    val category: String,
    val shopOrderPriority: Int,
    val categoryText: Map<String, String>,
    val gridPosition: GridPosition?,
    val canBeTrashed: Boolean,
    val image: String?
)