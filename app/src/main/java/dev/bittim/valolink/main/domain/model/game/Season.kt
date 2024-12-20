package dev.bittim.valolink.main.domain.model.game

import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Season(
    val uuid: String,
    val displayName: String,
    override val startTime: Instant,
    override val endTime: Instant,
) : ContentRelation() {
    override fun calcRemainingDays(): Int? {
        val days = Instant.now().until(
            endTime,
            ChronoUnit.DAYS
        ).toInt()
        return if (days < 0) null else days
    }
}