/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SprayEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.Spray

@Entity(
    tableName = "Sprays",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class SprayEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val themeUuid: String?,
    val hideIfNotOwned: Boolean,
    val displayIcon: String,
    val fullIcon: String?,
    val fullTransparentIcon: String?,
    val animationPng: String?,
    val animationGif: String?,
) : VersionedEntity {
    fun toType(): Spray {
        return Spray(
            uuid = uuid,
            displayName = displayName,
            themeUuid = themeUuid,
            hideIfNotOwned = hideIfNotOwned,
            displayIcon = displayIcon,
            fullIcon = fullIcon,
            fullTransparentIcon = fullTransparentIcon,
            animationPng = animationPng,
            animationGif = animationGif
        )
    }
}
