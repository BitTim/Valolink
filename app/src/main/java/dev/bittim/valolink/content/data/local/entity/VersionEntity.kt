/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       VersionEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.domain.model.Version

@Entity(
    tableName = "Version",
    indices = [
        Index(
            value = ["uuid"],
            unique = true
        )
    ]
)
data class VersionEntity(
    @PrimaryKey override val uuid: String,
    val manifestId: String,
    val branch: String,
    override val version: String,
    val buildVersion: String,
    val engineVersion: String,
    val riotClientVersion: String,
    val riotClientBuild: String,
    val buildDate: String,
) : VersionedEntity {
    fun toType(): Version {
        return Version(
            manifestId = manifestId,
            branch = branch,
            version = version,
            buildVersion = buildVersion,
            engineVersion = engineVersion,
            riotClientVersion = riotClientVersion,
            riotClientBuild = riotClientBuild,
            buildDate = buildDate,
        )
    }
}
