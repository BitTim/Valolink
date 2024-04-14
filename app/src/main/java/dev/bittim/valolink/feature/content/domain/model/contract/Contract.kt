package dev.bittim.valolink.feature.content.domain.model.contract

data class Contract(
    val uuid: String,
    val displayName: String,
    val displayIcon: String?,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val freeRewardScheduleUuid: String,
    val content: Content,
) {
    data class Content(
        val relationType: String?,
        val relationUuid: String?,
        val premiumRewardScheduleUuid: String?,
        val premiumVPCost: Int,
        val chapters: List<Chapter>
    )
}