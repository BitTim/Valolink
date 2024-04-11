package dev.bittim.valolink.feature.content.data.mapper

import dev.bittim.valolink.feature.content.data.local.SeasonEntity
import dev.bittim.valolink.feature.content.data.remote.SeasonDto
import dev.bittim.valolink.feature.content.domain.model.Season
import java.time.ZonedDateTime

fun SeasonDto.toSeasonEntity(version: String): SeasonEntity {
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

fun SeasonEntity.toSeason(): Season {
    return Season(
        uuid = uuid,
        name = displayName,
        startTime = ZonedDateTime.parse(startTime),
        endTime = ZonedDateTime.parse(endTime)
    )
}