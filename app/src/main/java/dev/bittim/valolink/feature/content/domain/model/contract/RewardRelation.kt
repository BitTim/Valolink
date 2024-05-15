package dev.bittim.valolink.feature.content.domain.model.contract

data class RewardRelation(
    val uuid: String,
    val type: String, val displayIcon: String, val displayName: String, val amount: Int
)