package dev.bittim.valolink.feature.content.domain.model.contract

import java.time.Instant

abstract class ContentRelation {
    abstract val startTime: Instant?
    abstract val endTime: Instant?

    abstract fun calcRemainingDays(): Int?
}