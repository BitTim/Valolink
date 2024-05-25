package dev.bittim.valolink.feature.main.domain.model.game.contract

data class Reward(
    val rewardType: String, val rewardUuid: String, val amount: Int, val isHighlighted: Boolean
)
