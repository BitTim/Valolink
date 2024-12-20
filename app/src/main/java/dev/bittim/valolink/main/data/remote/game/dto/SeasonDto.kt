package dev.bittim.valolink.main.data.remote.game.dto

import dev.bittim.valolink.main.data.local.game.entity.SeasonEntity

data class SeasonDto(
    val uuid: String,
    val displayName: String,
    val type: String?,
    val startTime: String,
    val endTime: String,
    val parentUuid: String?,
    val assetPath: String,
) {
    fun toEntity(version: String): SeasonEntity {
        return SeasonEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            type = type,
            startTime = startTime,
            endTime = endTime,
            parentUuid = parentUuid
        )
    }
}