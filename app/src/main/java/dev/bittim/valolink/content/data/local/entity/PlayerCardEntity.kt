/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerCardEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.PlayerCard

@Entity(
    tableName = "PlayerCards",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class PlayerCardEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val smallArt: String,
    val wideArt: String,
    val largeArt: String?,
) : VersionedEntity {
    fun toType(): PlayerCard {
        return PlayerCard(
            uuid = uuid,
            displayName = displayName,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
            themeUuid = themeUuid,
            displayIcon = displayIcon,
            smallArt = smallArt,
            wideArt = wideArt,
            largeArt = largeArt
        )
    }
}
