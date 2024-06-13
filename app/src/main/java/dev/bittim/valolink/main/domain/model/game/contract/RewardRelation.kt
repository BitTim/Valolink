package dev.bittim.valolink.main.domain.model.game.contract

data class RewardRelation(
    val uuid: String,
    val type: String,
    val displayIcon: String,
    val displayName: String,
    val amount: Int,
)