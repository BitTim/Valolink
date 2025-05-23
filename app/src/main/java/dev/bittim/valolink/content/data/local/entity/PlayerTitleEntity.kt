/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerTitleEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.PlayerTitle

@Entity(
    tableName = "PlayerTitles",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class PlayerTitleEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val displayName: String?,
    val titleText: String?,
    val isHiddenIfNotOwned: Boolean,
) : VersionedEntity {
    fun toType(): PlayerTitle {
        return PlayerTitle(
            uuid = uuid,
            displayName = displayName,
            titleText = titleText,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
        )
    }
}
