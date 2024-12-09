package dev.bittim.valolink.content.data.remote.dto.contract

import dev.bittim.valolink.content.data.local.entity.contract.ContractEntity

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