/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Agent.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.agent

import dev.bittim.valolink.content.domain.model.contract.content.ContentRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.user.domain.model.UserAgent
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

data class Agent(
    override val uuid: String,
    override val displayName: String,
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
) : ContentRelation {
    override fun calcRemainingDays(): Int? {
        if (startTime == null || endTime == null) return null

        val now = Instant.now()
        if (now.isBefore(startTime)) return null

        val days = now.until(endTime, ChronoUnit.DAYS).toInt()
        return if (days < 0) null else days
    }

    fun toUserObj(uid: String): UserAgent {
        return UserAgent(
            UUID.randomUUID().toString(),
            uid,
            uuid
        )
    }

    fun toRewardRelation(): RewardRelation {
        return RewardRelation(
            uuid,
            RewardType.AGENT,
            1,
            displayName,
            displayIcon,
            listOf(fullPortrait to null)
        )
    }

    companion object {
        val EMPTY = Agent(
            uuid = "",
            displayName = "",
            description = "",
            developerName = "",
            characterTags = null,
            displayIcon = "",
            displayIconSmall = "",
            bustPortrait = "",
            fullPortrait = "",
            fullPortraitV2 = "",
            killfeedPortrait = "",
            background = "",
            backgroundGradientColors = emptyList(),
            isFullPortraitRightFacing = false,
            isAvailableForTest = false,
            isBaseContent = false,
            role = Role.EMPTY,
            abilities = emptyList(),
            startTime = null,
            endTime = null
        )
    }
}
