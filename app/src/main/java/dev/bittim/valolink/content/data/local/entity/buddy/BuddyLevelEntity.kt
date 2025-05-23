/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       BuddyLevelEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity.buddy

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.buddy.BuddyLevel

@Entity(
    tableName = "BuddyLevels", foreignKeys = [ForeignKey(
        entity = BuddyEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["buddy"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["buddy"], unique = false
    )]
)
data class BuddyLevelEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val buddy: String,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String,
) : VersionedEntity {
    fun toType(): BuddyLevel {
        return BuddyLevel(
            uuid = uuid,
            hideIfNotOwned = hideIfNotOwned,
            displayName = displayName,
            displayIcon = displayIcon
        )
    }
}
