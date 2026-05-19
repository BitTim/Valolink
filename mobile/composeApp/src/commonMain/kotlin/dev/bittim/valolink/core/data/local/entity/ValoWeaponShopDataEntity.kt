/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoWeaponShopDataEntity.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:35
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.bittim.valolink.core.domain.model.GridPosition
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