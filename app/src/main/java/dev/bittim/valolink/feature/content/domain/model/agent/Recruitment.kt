package dev.bittim.valolink.feature.content.domain.model.agent

data class Recruitment(
    val counterId: String,
    val milestoneId: String,
    val milestoneThreshold: Int,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Int,
    val startDate: String,
    val endDate: String
)
