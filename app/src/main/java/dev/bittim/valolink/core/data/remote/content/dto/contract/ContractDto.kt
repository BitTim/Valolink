package dev.bittim.valolink.core.data.remote.content.dto.contract

import dev.bittim.valolink.core.data.local.content.entity.contract.ContractEntity

data class ContractDto(
	val uuid: String,
	val displayName: String,
	val displayIcon: String?,
	val shipIt: Boolean,
	val useLevelVPCostOverride: Boolean,
	val levelVPCostOverride: Int,
	val freeRewardScheduleUuid: String,
	val content: ContentDto,
	val assetPath: String,
) {
	fun toEntity(version: String): ContractEntity {
		return ContractEntity(
			uuid,
			version,
			displayName,
			displayIcon,
			shipIt,
			useLevelVPCostOverride,
			levelVPCostOverride,
			freeRewardScheduleUuid,
			assetPath
		)
	}
}