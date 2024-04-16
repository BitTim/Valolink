package dev.bittim.valolink.feature.content.domain.model.contract

data class Contract(
    val uuid: String,
    val displayName: String,
    val displayIcon: String?,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val freeRewardScheduleUuid: String,
    val content: Content, val relation: ContractRelation?
) {
    fun calcTotalXp(): Int {
        return content.chapters.map { chapter ->
            chapter.levels.map { level ->
                level.xp
            }
        }.flatten().sum()
    }

    fun calcRemainingDays(): Int? {
        return null
    }
}