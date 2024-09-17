package dev.bittim.valolink.main.domain.model.game.contract.reward

data class Reward(
    val rewardType: String,
    val rewardUuid: String,
    val amount: Int,
    val isHighlighted: Boolean,
    val isFreeReward: Boolean,
    val relation: RewardRelation?,
)
