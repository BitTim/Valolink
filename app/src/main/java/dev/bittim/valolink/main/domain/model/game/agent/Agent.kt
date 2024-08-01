package dev.bittim.valolink.main.domain.model.game.agent

import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation
import dev.bittim.valolink.main.domain.model.user.UserAgent
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

data class Agent(
    val uuid: String,
    val displayName: String,
    val description: String,
    val developerName: String,
    val characterTags: List<String>?,
    val displayIcon: String,
    val displayIconSmall: String,
    val bustPortrait: String,
    val fullPortrait: String,
    val fullPortraitV2: String,
    val killfeedPortrait: String,
    val background: String,
    val backgroundGradientColors: List<String>,
    val isFullPortraitRightFacing: Boolean,
    val isAvailableForTest: Boolean,
    val isBaseContent: Boolean,
    val role: Role,
    val abilities: List<Ability>,

    override val startTime: Instant? = null,
    override val endTime: Instant? = null,
) : ContentRelation() {

    override fun calcRemainingDays(): Int? {
        if (startTime == null || endTime == null) return null

        val days = Instant.now().until(
            endTime,
            ChronoUnit.DAYS
        ).toInt()
        return if (days < 0) null else days
    }

    fun toUserObj(uid: String): UserAgent {
        return UserAgent(
            UUID.randomUUID().toString(),
            uid,
            uuid
        )
    }
}
