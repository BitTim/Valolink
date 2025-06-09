/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ModeEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.mode.Mode
import dev.bittim.valolink.content.domain.model.mode.ScoreType

@Entity(
    tableName = "Modes",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class ModeEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val displayName: String,
    val description: String?,
    val scoreType: ScoreType,
    val canBeRanked: Boolean,
    val duration: String?,
    val roundsPerHalf: Int,
    val displayIcon: String?,
    val listViewIconTall: String?
) : VersionedEntity {
    fun toType(): Mode {
        return Mode(
            uuid,
            displayName,
            description,
            scoreType,
            canBeRanked,
            duration,
            roundsPerHalf,
            displayIcon,
            listViewIconTall
        )
    }
}
