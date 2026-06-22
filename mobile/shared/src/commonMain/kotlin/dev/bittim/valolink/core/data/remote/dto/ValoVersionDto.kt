/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersionDto.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 16:57
 */

package dev.bittim.valolink.core.data.remote.dto

import dev.bittim.valolink.core.domain.model.ValoVersion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ValoVersionDto(
    val id: Int,
    val branch: String,
    @SerialName("build_version") val buildVersion: String,
    @SerialName("manifest_id") val manifestId: String,
    @SerialName("riot_client_build") val riotClientBuild: String,
    @SerialName("riot_client_version") val riotClientVersion: String,
    @SerialName("engine_version") val engineVersion: String,
    val version: String,
    @SerialName("build_date") val buildDate: Instant
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
            buildDate = buildDate
        )
    }

}
