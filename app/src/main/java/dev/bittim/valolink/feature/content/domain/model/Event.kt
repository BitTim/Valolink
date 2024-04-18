package dev.bittim.valolink.feature.content.domain.model

import dev.bittim.valolink.feature.content.domain.model.contract.ContentRelation
import java.time.ZonedDateTime

data class Event(
    val uuid: String,
    val displayName: String,
    val shortDisplayName: String,
    override val startTime: ZonedDateTime,
    override val endTime: ZonedDateTime,
) : ContentRelation()
