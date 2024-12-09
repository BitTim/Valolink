package dev.bittim.valolink.content.data.remote.dto.agents

import dev.bittim.valolink.content.data.local.entity.agent.RecruitmentEntity

data class RecruitmentDto(
	val counterId: String,
	val milestoneId: String,
	val milestoneThreshold: Int,
	val useLevelVpCostOverride: Boolean,
	val levelVpCostOverride: Int,
	val startDate: String,
	val endDate: String,
) {
	fun toEntity(version: String): RecruitmentEntity {
		return RecruitmentEntity(
			milestoneId,
			version,
			milestoneThreshold,
			useLevelVpCostOverride,
			levelVpCostOverride,
			startDate,
			endDate
		)
	}
}