package dev.bittim.valolink.content.domain.model.contract.reward

data class Reward(
    val rewardType: String,
    val rewardUuid: String,
    val amount: Int,
    val isHighlighted: Boolean,
    val isFreeReward: Boolean,
    val relation: RewardRelation?,
)
