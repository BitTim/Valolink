package dev.bittim.valolink.feature.content.domain.model.agent

import dev.bittim.valolink.feature.content.domain.model.contract.ContractRelation
import java.time.ZonedDateTime

data class Agent(
    val uuid: String,
    override val displayName: String,
    val description: String,
    val developerName: String,
    val characterTags: List<String>?,
    override val displayIcon: String,
    val displayIconSmall: String,
    val bustPortrait: String,
    val fullPortrait: String,
    val fullPortraitV2: String,
    val killfeedPortrait: String,
    override val background: String,
    override val backgroundGradientColors: List<String>,
    val isFullPortraitRightFacing: Boolean,
    val isAvailableForTest: Boolean,
    val isBaseContent: Boolean,
    val role: Role,
    val recruitment: Recruitment?,
    val abilities: List<Ability>,

    override val startTime: ZonedDateTime? = null,
    override val endTime: ZonedDateTime? = null
) : ContractRelation()
