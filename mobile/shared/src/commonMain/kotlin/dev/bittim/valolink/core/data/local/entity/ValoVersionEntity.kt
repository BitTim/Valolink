/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersionEntity.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 14:03
 */

package dev.bittim.valolink.core.data.local.entity

import androidx.room.Entity
import dev.bittim.valolink.core.domain.model.ValoVersion
import kotlin.time.Instant

@Entity(
    tableName = "valo_version",
    primaryKeys = ["id"]
)
data class ValoVersionEntity(
    val id: Int,
    val branch: String,
    val buildVersion: String,
    val manifestId: String,
    val riotClientBuild: String,
    val riotClientVersion: String,
    val engineVersion: String,
    val version: String,
    val buildDate: Instant
) {
    fun toModel(): ValoVersion {
        return ValoVersion(
            branch = branch,
            buildVersion = buildVersion,
            manifestId = manifestId,
            riotClientBuild = riotClientBuild,
            riotClientVersion = riotClientVersion,
            engineVersion = engineVersion,
            version = version,
            buildDate = buildDate,
        )
    }

    companion object {
        fun fromModel(model: ValoVersion): ValoVersionEntity {
            return ValoVersionEntity(
                id = 0,
                branch = model.branch,
                buildVersion = model.buildVersion,
                manifestId = model.manifestId,
                riotClientBuild = model.riotClientBuild,
                riotClientVersion = model.riotClientVersion,
                engineVersion = model.engineVersion,
                version = model.version,
                buildDate = model.buildDate,
            )
        }
    }
}