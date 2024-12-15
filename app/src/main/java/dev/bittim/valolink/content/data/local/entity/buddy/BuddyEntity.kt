/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       BuddyEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity.buddy

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.buddy.Buddy
import dev.bittim.valolink.content.domain.model.buddy.BuddyLevel

@Entity(
    tableName = "Buddies",
    indices = [
        Index(
            value = ["uuid"], unique = true
        ),
    ]
)
data class BuddyEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val assetPath: String,
) : VersionedEntity {
    fun toType(levels: List<BuddyLevel>): Buddy {
        return Buddy(
            uuid = uuid,
            displayName = displayName,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
            themeUuid = themeUuid,
            displayIcon = displayIcon,
            levels = levels
        )
    }
}
