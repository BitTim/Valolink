package dev.bittim.valolink.feature.main.domain.model.game.agent

data class Recruitment(
    val uuid: String, val xp: Int,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Int,
    val startDate: String,
    val endDate: String
)
