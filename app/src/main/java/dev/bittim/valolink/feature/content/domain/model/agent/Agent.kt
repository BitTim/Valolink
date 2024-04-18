package dev.bittim.valolink.feature.content.domain.model.agent

import dev.bittim.valolink.feature.content.domain.model.contract.ContentRelation
import java.time.ZonedDateTime

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

    override val startTime: ZonedDateTime? = null,
    override val endTime: ZonedDateTime? = null
) : ContentRelation()
