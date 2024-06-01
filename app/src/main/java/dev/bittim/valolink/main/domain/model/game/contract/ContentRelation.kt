package dev.bittim.valolink.main.domain.model.game.contract

import java.time.Instant

abstract class ContentRelation {
    abstract val startTime: Instant?
    abstract val endTime: Instant?

    abstract fun calcRemainingDays(): Int?
}