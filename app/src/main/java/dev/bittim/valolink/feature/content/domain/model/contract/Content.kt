package dev.bittim.valolink.feature.content.domain.model.contract

data class Content(
    val relationType: String?,
    val relationUuid: String?,
    val premiumRewardScheduleUuid: String?,
    val premiumVPCost: Int,
    val chapters: List<Chapter>
)