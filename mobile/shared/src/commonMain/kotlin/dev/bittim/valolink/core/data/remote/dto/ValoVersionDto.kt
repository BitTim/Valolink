/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersionDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:30
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.ValoVersion
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Suppress("PropertyName")
@Serializable
data class ValoVersionDto(
    val id: Int,
    val branch: String,
    val build_version: String,
    val manifest_id: String,
    val riot_client_build: String,
    val riot_client_version: String,
    val engine_version: String,
    val version: String,
    val build_date: Instant
) {
    fun toModel(): ValoVersion {
        return ValoVersion(
            branch = branch,
            buildVersion = build_version,
            manifestId = manifest_id,
            riotClientBuild = riot_client_build,
            riotClientVersion = riot_client_version,
            engineVersion = engine_version,
            version = version,
            buildDate = build_date
        )
    }

}
