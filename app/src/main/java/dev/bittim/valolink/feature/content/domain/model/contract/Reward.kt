package dev.bittim.valolink.feature.content.domain.model.contract

data class Reward(
    val rewardType: String?, val rewardUuid: String?, val amount: Int, val isHighlighted: Boolean,

    val relation: RewardRelation?
)
