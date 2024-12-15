/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RecruitmentDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

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