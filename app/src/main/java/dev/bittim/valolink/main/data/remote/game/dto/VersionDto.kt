package dev.bittim.valolink.main.data.remote.game.dto

import dev.bittim.valolink.main.data.local.game.entity.VersionEntity

data class VersionDto(
    val manifestId: String,
    val branch: String,
    val version: String,
    val buildVersion: String,
    val engineVersion: String,
    val riotClientVersion: String,
    val riotClientBuild: String,
    val buildDate: String,
) {
    fun toEntity(): VersionEntity {
        return VersionEntity(
            id = 0,
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
