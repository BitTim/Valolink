package dev.bittim.valolink.main.domain.model.game.contract.reward

data class RewardRelation(
    val uuid: String,
    val type: RewardType,
    val previewIcon: String,
    val displayIcon: String? = null,
    val displayName: String,
    val amount: Int,
)