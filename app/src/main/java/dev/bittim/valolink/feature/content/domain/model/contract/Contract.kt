package dev.bittim.valolink.feature.content.domain.model.contract

import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class Contract(
    val uuid: String,
    val displayName: String,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val content: Content,
    val startTime: ZonedDateTime?,
    val endTime: ZonedDateTime?
) {
    fun isActive(): Boolean {
        return startTime != null && endTime != null && ZonedDateTime.now()
            .isAfter(startTime) && ZonedDateTime.now().isBefore(endTime)
    }

    fun isAgentGear(): Boolean {
        return startTime == null && endTime == null && content.relation is Agent
    }

    fun isInactive(): Boolean {
        return endTime != null && ZonedDateTime.now().isAfter(endTime)
    }
    
    fun calcTotalXp(): Int {
        return content.chapters.map { chapter ->
            chapter.levels.map { level ->
                level.xp
            }
        }.flatten().sum()
    }

    fun calcRemainingDays(): Int? {
        if (startTime == null && endTime == null) return null
        val days = ZonedDateTime.now().until(endTime, ChronoUnit.DAYS).toInt()
        return if (days < 0) null else days
    }
}