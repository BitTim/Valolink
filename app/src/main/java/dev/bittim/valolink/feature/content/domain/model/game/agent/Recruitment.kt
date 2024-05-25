package dev.bittim.valolink.feature.content.domain.model.game.agent

data class Recruitment(
    val uuid: String, val xp: Int,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Int,
    val startDate: String,
    val endDate: String
)
