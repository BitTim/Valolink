package dev.bittim.valolink.feature.content.domain.model

import java.time.ZonedDateTime

data class Season(
    val uuid: String,
    val name: String,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime
)