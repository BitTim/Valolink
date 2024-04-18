package dev.bittim.valolink.feature.content.domain.model.contract

import java.time.ZonedDateTime

abstract class ContentRelation {
    abstract val startTime: ZonedDateTime?
    abstract val endTime: ZonedDateTime?
}