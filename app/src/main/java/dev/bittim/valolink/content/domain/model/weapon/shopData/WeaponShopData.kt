/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponShopData.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.weapon.shopData

data class WeaponShopData(
    val cost: Int,
    val category: String,
    val shopOrderPriority: Int,
    val categoryText: String,
    val gridPosition: WeaponGridPosition?,
    val canBeTrashed: Boolean,
    val image: String?,
    val newImage: String,
    val newImage2: String?,
)
