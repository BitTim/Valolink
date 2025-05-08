/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FlexEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 13:39
 */

package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.Flex

@Entity(
    tableName = "Flexes",
    indices = [androidx.room.Index(
        value = ["uuid"],
        unique = true
    )]
)
data class FlexEntity(
    @PrimaryKey override val uuid: String,
    override val version: String,
    val displayName: String,
    val displayIcon: String,
) : VersionedEntity {
    fun toType(): Flex {
        return Flex(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon
        )
    }
}
