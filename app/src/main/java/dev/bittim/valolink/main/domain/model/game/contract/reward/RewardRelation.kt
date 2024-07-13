package dev.bittim.valolink.main.domain.model.game.contract.reward

data class RewardRelation(
    val uuid: String,
    val type: RewardType,
    val amount: Int,
    val displayName: String,
    val previewImage: String,
    val displayIcon: String,
    val background: String? = null,
)