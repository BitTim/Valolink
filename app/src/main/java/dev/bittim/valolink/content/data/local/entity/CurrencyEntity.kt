/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CurrencyEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.Currency

@Entity(
    tableName = "Currencies",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class CurrencyEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val displayNameSingular: String,
    val displayIcon: String,
    val largeIcon: String,
) : VersionedEntity {
    fun toType(): Currency {
        return Currency(
            uuid,
            displayName,
            displayNameSingular,
            displayIcon,
            largeIcon
        )
    }
}