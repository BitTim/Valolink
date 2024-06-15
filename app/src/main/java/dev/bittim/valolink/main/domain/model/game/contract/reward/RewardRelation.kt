package dev.bittim.valolink.main.domain.model.game.contract.reward

data class RewardRelation(
    val uuid: String,
    val type: RewardType,
    val displayIcon: String,
    val displayName: String,
    val amount: Int,
) {}